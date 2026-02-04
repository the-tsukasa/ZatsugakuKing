package com.example.kadai09_pi12a_23

import android.app.Dialog
import android.graphics.Outline
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.IOException

class QuizActivity : AppCompatActivity() {

    private data class QuizQuestion(
        val id: Int,
        val text: String,
        val options: List<String>,
        val correctIndex: Int,
        val genre: String
    )

    private lateinit var questionText: TextView
    private lateinit var progressText: TextView
    private lateinit var statusText: TextView
    private lateinit var quizUserAvatar: ImageView
    private lateinit var quizUserLevel: TextView
    private lateinit var quizExpText: TextView
    private lateinit var quizExpBar: android.widget.ProgressBar
    private lateinit var quizExpHint: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var optionButtons: List<RadioButton>
    private lateinit var answerButton: Button
    private lateinit var resetButton: Button
    private lateinit var topButton: Button

    private var allQuestions: List<QuizQuestion> = emptyList()
    private var questions: List<QuizQuestion> = emptyList()
    private var currentIndex = 0
    private var totalAnswered = 0
    private var correctCount = 0
    private var isFinished = false
    private var totalQuestions = 0
    private var showingFeedback = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
            val verticalDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
            v.setPadding(
                systemBars.left + horizontalDp,
                systemBars.top + verticalDp,
                systemBars.right + horizontalDp,
                systemBars.bottom + verticalDp
            )
            insets
        }

        questionText = findViewById(R.id.questionText)
        progressText = findViewById(R.id.progressText)
        statusText = findViewById(R.id.statusText)
        quizUserAvatar = findViewById(R.id.quiz_user_avatar)
        quizUserLevel = findViewById(R.id.quiz_user_level)
        quizExpText = findViewById(R.id.quiz_exp_text)
        quizExpBar = findViewById(R.id.quiz_exp_bar)
        quizExpHint = findViewById(R.id.quiz_exp_hint)
        radioGroup = findViewById(R.id.answerGroup)
        quizUserAvatar.post {
            quizUserAvatar.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            quizUserAvatar.clipToOutline = true
        }
        updateUserBar()
        answerButton = findViewById(R.id.answerButton)
        resetButton = findViewById(R.id.resetButton)
        topButton = findViewById(R.id.topButton)

        optionButtons = listOf(
            findViewById(R.id.option1),
            findViewById(R.id.option2),
            findViewById(R.id.option3),
            findViewById(R.id.option4)
        )

        var loaded = loadQuestionsFromAssets()
        if (loaded.isEmpty()) {
            loaded = createFallbackQuestions()
        }
        val selectedGenre = intent.getStringExtra(EXTRA_GENRE) ?: Genre.ALL
        allQuestions = if (selectedGenre == Genre.ALL) {
            loaded
        } else {
            loaded.filter { it.genre == selectedGenre }
        }
        if (allQuestions.isEmpty()) {
            Toast.makeText(this, getString(R.string.genre_no_questions), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        resetQuiz()

        radioGroup.setOnCheckedChangeListener { _, _ -> updateOptionBackgrounds() }

        answerButton.setOnClickListener {
            if (isFinished) {
                Toast.makeText(this, getString(R.string.quiz_finished_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkedId = radioGroup.checkedRadioButtonId
            if (checkedId == View.NO_ID) {
                Toast.makeText(this, getString(R.string.select_answer_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = optionButtons.indexOfFirst { it.id == checkedId }
            val currentQuestion = questions[currentIndex]
            val isCorrect = selectedIndex == currentQuestion.correctIndex

            totalAnswered += 1
            if (isCorrect) {
                correctCount += 1
            }

            val rankBefore = RankManager.getCurrentRank(this)
            if (isCorrect) {
                RankManager.addCorrect(this)
                var expGain = 10
                val consecutive = RankManager.getConsecutiveCorrect(this)
                when {
                    consecutive >= 10 -> expGain += 50
                    consecutive >= 5 -> expGain += 20
                    consecutive >= 3 -> expGain += 10
                }
                RankManager.addExp(this, expGain)
                val rankAfter = RankManager.getCurrentRank(this)
                Toast.makeText(this, getString(R.string.result_correct), Toast.LENGTH_SHORT).show()
                if (rankAfter != rankBefore) {
                    updateUserBar()
                    showLevelUpDialog(rankAfter)
                }
            } else {
                RankManager.addIncorrect(this)
                Toast.makeText(this, getString(R.string.result_incorrect), Toast.LENGTH_SHORT).show()
            }

            val isLast = currentIndex == questions.lastIndex
            if (isLast && correctCount == totalQuestions && totalAnswered == totalQuestions) {
                val allCorrectBonus = when (totalQuestions) {
                    5 -> 20
                    10 -> 50
                    20 -> 120
                    else -> 0
                }
                if (allCorrectBonus > 0) {
                    RankManager.addExp(this, allCorrectBonus)
                    Toast.makeText(
                        this,
                        getString(R.string.toast_all_correct_bonus, allCorrectBonus),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            updateStatus()
            updateProgress()

            showingFeedback = true
            answerButton.isEnabled = false
            radioGroup.isEnabled = false
            applyResultBackgrounds(currentQuestion.correctIndex, selectedIndex, isCorrect)
            if (!isLast) updateUserBar()
            radioGroup.postDelayed({
                showingFeedback = false
                if (isLast) {
                    isFinished = true
                    updateUserBar()
                    showFinishedState()
                } else {
                    currentIndex += 1
                    updateQuestion()
                    answerButton.isEnabled = true
                    radioGroup.isEnabled = true
                }
            }, 1200L)
        }

        resetButton.setOnClickListener {
            resetQuiz()
        }

        topButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::quizUserAvatar.isInitialized) updateUserBar()
    }

    /** 2️⃣ 升级弹窗：🎉 Lv.UP! / 解锁新称号 / 解锁新头像边框 */
    private fun showLevelUpDialog(newRank: RankManager.Rank) {
        val levelUpDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_level_up)
            setCancelable(true)
        }
        levelUpDialog.findViewById<Button>(R.id.dialog_level_up_confirm).setOnClickListener { levelUpDialog.dismiss() }
        levelUpDialog.show()
    }

    private fun updateUserBar() {
        val avatarResId = ProfileManager.getAvatarResId(this)
        quizUserAvatar.setImageResource(
            if (avatarResId != 0) avatarResId else R.drawable.avatar_default
        )
        val level = RankManager.getCurrentLevel(this)
        quizUserLevel.text = getString(R.string.exp_level_title_format, level, RankManager.getRankTitleOnly(this))
        val totalExp = RankManager.getTotalExp(this)
        val expCurrentLevel = RankManager.getExpForCurrentLevel(this)
        val expNextLevel = RankManager.getExpForNextLevel(this)
        quizExpText.text = getString(R.string.exp_bar_format, totalExp, expNextLevel)
        if (RankManager.isMaxLevel(this)) {
            quizExpBar.max = 100
            quizExpBar.progress = 100
            quizExpHint.visibility = View.GONE
        } else {
            val range = (expNextLevel - expCurrentLevel).coerceAtLeast(1)
            val progress = ((totalExp - expCurrentLevel).coerceIn(0, range) * 100 / range).toInt()
            quizExpBar.max = 100
            quizExpBar.progress = progress
            val questionsLeft = RankManager.questionsUntilNextLevel(this)
            if (questionsLeft in 1..99) {
                quizExpHint.text = getString(R.string.exp_progress_hint, questionsLeft, level + 1)
                quizExpHint.visibility = View.VISIBLE
            } else {
                quizExpHint.visibility = View.GONE
            }
        }
    }

    private fun loadQuestionsFromAssets(): List<QuizQuestion> {
        return try {
            val jsonText = assets.open("quiz_questions.json").bufferedReader().use { it.readText() }
            val root = JSONObject(jsonText)
            val array = root.getJSONArray("questions")
            val list = mutableListOf<QuizQuestion>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val optionsArray = obj.getJSONArray("options")
                val options = List(optionsArray.length()) { index ->
                    optionsArray.getString(index)
                }
                list.add(
                    QuizQuestion(
                        id = obj.getInt("id"),
                        text = obj.getString("text"),
                        options = options,
                        correctIndex = obj.getInt("correctIndex"),
                        genre = obj.optString("genre", Genre.JAPAN)
                    )
                )
            }
            list
        } catch (_: IOException) {
            emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun createFallbackQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                text = "2026年のサッカーワールドカップの開催地に含まれない国は？",
                options = listOf("ブラジル", "カナダ", "メキシコ", "アメリカ"),
                correctIndex = 0,
                genre = Genre.WORLD
            ),
            QuizQuestion(
                id = 2,
                text = "日本の通貨の単位は？",
                options = listOf("ウォン", "ドル", "円", "ユーロ"),
                correctIndex = 2,
                genre = Genre.JAPAN
            ),
            QuizQuestion(
                id = 3,
                text = "日本の首都は？",
                options = listOf("東京", "大阪", "名古屋", "札幌"),
                correctIndex = 0,
                genre = Genre.JAPAN
            )
        )
    }

    companion object {
        const val EXTRA_GENRE = "extra_genre"
    }

    private fun updateOptionBackgrounds() {
        if (showingFeedback) return
        val checkedId = radioGroup.checkedRadioButtonId
        optionButtons.forEach { button ->
            button.setBackgroundResource(
                if (button.id == checkedId) R.drawable.bg_option_pill_selected
                else R.drawable.bg_option_pill
            )
        }
    }

    private fun applyResultBackgrounds(correctIndex: Int, selectedIndex: Int, isCorrect: Boolean) {
        optionButtons.forEachIndexed { index, button ->
            when {
                index == correctIndex -> button.setBackgroundResource(R.drawable.bg_result_correct)
                !isCorrect && index == selectedIndex -> button.setBackgroundResource(R.drawable.bg_result_incorrect)
                else -> button.setBackgroundResource(R.drawable.bg_option_pill)
            }
        }
    }

    private fun updateQuestion() {
        val question = questions[currentIndex]
        questionText.text = question.text

        optionButtons.forEachIndexed { index, button ->
            if (index < question.options.size) {
                button.text = question.options[index]
                button.visibility = View.VISIBLE
                button.setBackgroundResource(R.drawable.bg_option_pill)
            } else {
                button.visibility = View.GONE
            }
        }
        radioGroup.clearCheck()
        radioGroup.visibility = View.VISIBLE
        answerButton.isEnabled = true
        answerButton.visibility = View.VISIBLE
        topButton.visibility = View.GONE
        updateProgress()
    }

    private fun updateStatus() {
        statusText.text = getString(
            R.string.status_format,
            correctCount
        )
    }

    private fun updateProgress() {
        val currentNumber = if (isFinished) totalQuestions else currentIndex + 1
        progressText.text = getString(R.string.progress_format, currentNumber, totalQuestions)
    }

    private fun showFinishedState() {
        questionText.text = getString(R.string.quiz_complete_text)
        radioGroup.visibility = View.GONE
        answerButton.isEnabled = false
        answerButton.visibility = View.GONE
        topButton.visibility = View.VISIBLE
    }

    private fun resetQuiz() {
        questions = allQuestions.shuffled().take(5)
        currentIndex = 0
        totalAnswered = 0
        correctCount = 0
        isFinished = false
        totalQuestions = questions.size
        if (questions.isEmpty()) return
        updateQuestion()
        updateStatus()
        updateProgress()
    }
}
