package com.example.buildupfrontend.SignupActivity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.Patterns
import com.example.buildupfrontend.R
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.onBackPressedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Area(_title: String, _checked: Boolean) {
    val title: String = _title
    var checked: Boolean = _checked

    fun check(b:Boolean) {
        checked = b
    }
}

@AndroidEntryPoint
open class FragmentSU3: Fragment(), View.OnClickListener, onBackPressedListener, Patterns {
    var mView: View? = null
    private val viewModel : SignupViewModel by activityViewModels()
    private lateinit var mDialogView: View

    lateinit var clName: ConstraintLayout
    lateinit var clEmail: ConstraintLayout
    lateinit var clSchool: ConstraintLayout
    lateinit var clMajor: ConstraintLayout
    lateinit var clGrade: ConstraintLayout

    lateinit var tlName: TextInputLayout
    lateinit var tlEmail: TextInputLayout
    lateinit var tlSchool: TextInputLayout
    lateinit var tlMajor: TextInputLayout
    lateinit var tlGrade: TextInputLayout

    lateinit var etName: TextInputEditText
    lateinit var etEmail: TextInputEditText
    lateinit var etSchool: TextInputEditText
    lateinit var etMajor: TextInputEditText
    lateinit var ddGrade: AutoCompleteTextView
    lateinit var btnArea: AppCompatImageButton
    lateinit var btnOk: AppCompatButton

    private lateinit var selectedArea1: TextView
    private lateinit var selectedArea2: TextView
    private lateinit var selectedArea3: TextView
    private var checkedList: ArrayList<String> = arrayListOf()

    private val areaMap = HashMap<Int, Area>() // Button ID-Title-checked
    private val gradeArrayList: MutableList<String> = arrayListOf("1","2","3","4","5","6","졸업 및 수료","기타")
    private val areaTitleList: MutableList<String> = arrayListOf("#경영·사무","#회계·재무·세무",
                                                                 "#홍보·광고마케팅","#영업","#생산·제조",
                                                                 "#건설·건축","#무역·유통","#공공·복지",
                                                                 "#IT개발·데이터","#디자인","#금융",
                                                                 "#의료·보건","#교육","#전문·특수직",
                                                                 "#미디어·문화·스포츠","#기획·전략",
                                                                 "#연구·R&D","#인사·노무·HRD",
                                                                 "#상품기획·MD","#엔지니어링·설계")
    private val areaMax: Int = 3 // 선택 가능한 관심분야 최대 개수
    private var areaCount: Int = 0
    private lateinit var allButtons: ArrayList<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mView = inflater.inflate(R.layout.fragment_su3, container, false)!!
        initView(mView!!)

        // 타입 검사
        checkTypes()
        // 학년 dropdown 관리
        setDropdown()

        // 관심분야 선택: Dialog
        btnArea.setOnClickListener {
            // Dialog View 보이기
            mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_area, null)
            val mAlertDialog = showDialog(this.context)
            val btnSave = mDialogView.findViewById<AppCompatButton>(R.id.btn_ok)
            val btnClose = mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
            val btnBack = mDialogView.findViewById<AppCompatButton>(R.id.btn_back)
            // checkedList에 있는 버튼들은 다 checked(다시 열었을 때 유지되게)
            recoverChecks(checkedList)

            // allButtons: button_area 내의 모든 버튼
            allButtons = mDialogView.findViewById<ConstraintLayout>(R.id.rl_button_area).touchables
            allButtons.setTitles(areaTitleList)// 분야 항목 이름, background 지정


            // 저장 버튼
            btnSave.setOnClickListener {
                // dialog 종료하면서 빈 값 확인 --> '가입 완료' 버튼 활성화
                enableOkBtn()

                // dialog 종료 후 시각화
                showArea(listOf<TextView>(selectedArea1, selectedArea2, selectedArea3), checkedList)

                mAlertDialog.dismiss()
            }

            // 닫기 버튼
            btnClose.setOnClickListener {
                mAlertDialog.dismiss()
            }
            btnBack.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        // 가입 완료
        btnOk.setOnClickListener {
            nextStep()
        }

        return mView
    }

    private fun showArea(tvList: List<TextView>, checkedArea: ArrayList<String>) {
        val tvIterator = tvList.iterator()
        val checkedIterator = checkedArea.iterator()

        for (i in 1..areaMax) {
            val nextArea = tvIterator.next()
            if (checkedIterator.hasNext()) {
                nextArea.visibility = View.VISIBLE
                nextArea.text = checkedIterator.next()
            } else {
              nextArea.visibility = View.GONE
            }
        }

    }

    private fun enableOkBtn() {
        btnOk.isEnabled = etName.text.toString().isNotEmpty() &&
                etEmail.text.toString().isNotEmpty() &&
                etSchool.text.toString().isNotEmpty() &&
                etMajor.text.toString().isNotEmpty() &&
                ddGrade.text.toString().isNotEmpty() &&
                checkedList.isNotEmpty()
    }

    /**
     * 선택 후 dialog 다시 켰을 때 선택된 값 유지
     */
    private fun recoverChecks(checkedArea: ArrayList<String>) {
        for ((id, area) in areaMap) {
            if (checkedArea.contains(area.title)) {
                Log.i("recoverChecked", area.title)
                mDialogView.findViewById<AppCompatToggleButton>(id).isChecked = true
            }
        }
    }

    /**
     * if areaCount exceed 3: disable unchecked areas (leave checked ones)
     * if not: enable all areas
     */
    private fun maxAreas(i: Int) {
        if (areaCount >= i) {
            for (btn in allButtons) {
                btn as AppCompatToggleButton
                btn.isEnabled = btn.isChecked
            }
        } else {
            for (btn in allButtons) {
                btn as AppCompatToggleButton
                btn.isEnabled = true
            }
        }
    }

    private fun checkTypes() {
        tlName.editText?.onFocusChangeListener = validInput(etName, tlName, KorEng, "* 한글, 영어만 입력해주세요.")
        tlEmail.editText?.onFocusChangeListener = validInput(etEmail, tlEmail, typeEmail, "* 올바른 이메일을 입력해주세요.")
        tlSchool.editText?.onFocusChangeListener = validInput(etSchool, tlSchool, KorEngSpace, "* 한글, 영어만 입력해주세요.")
        tlMajor.editText?.onFocusChangeListener = validInput(etMajor, tlMajor, KorEngSpace, "* 한글, 영어만 입력해주세요.")
    }

    /**
     * 1. show titles on ToggleButton
     * 2. set same 'onClick' method to all buttons
     * 3. create a HashMap betw. btn id & btn info
     */
    @SuppressLint("ResourceAsColor")
    private fun <E> ArrayList<E>.setTitles(areaTitleList: MutableList<String>) {
        for ((idx, title) in areaTitleList.withIndex()) {
            val btnArea = (this[idx] as AppCompatToggleButton)
            btnArea.setTitle(title)
            btnArea.setBackgroundResource(R.drawable.selector_ui_square)
            btnArea.setOnClickListener(this@FragmentSU3)
            areaMap[btnArea.id] = Area(title, false) // map (btn ID, title)
        }
    }

    /**
     * when a button is clicked
     * 1. save the checked state in 'Area' class instance
     * 2. update 'areaCount'
     */
    override fun onClick(v: View?) {
        val btn: AppCompatToggleButton = v as AppCompatToggleButton
        val isChecked = btn.isChecked
        areaMap[btn.id]?.check(isChecked)
        areaCount += updateCount(isChecked)
        updateCheckedList(isChecked, btn.id)

        // 최대 3개 선택 가능-관리
        maxAreas(areaMax)
    }

    /**
     * update checkedList everytime area is clicked
     */
    private fun updateCheckedList(isChecked: Boolean, btnId: Int) {
        if (isChecked) {
            checkedList.add(areaMap[btnId]?.title.toString())
        } else {
            checkedList.remove(areaMap[btnId]?.title.toString())
        }
    }

    /**
     * btn.isChecked = 0 -> -1
     * btn.isChecked = 1 -> +1
     */
    private fun updateCount(isChecked: Boolean): Int {
        return 2 * isChecked.toInt() - 1
    }

    override fun onBackPressed() {
        if (activity?.javaClass?.simpleName.toString() == "SignupActivity") {
            (activity as SignupActivity?)!!.nextFragment(2, FragmentSU2())
        } else if (activity?.javaClass?.simpleName.toString() == "LoginProfileActivity") {
            (activity as SignupActivity?)!!.nextFragment(1, FragmentSU1())
        }
    }

    private fun nextStep() {
        viewModel.userName = etName.text.toString()
        viewModel.userEmail = etEmail.text.toString()
        viewModel.userSchool = etSchool.text.toString()
        viewModel.userMajor = etMajor.text.toString()
        viewModel.userGrade = ddGrade.text.toString()
        viewModel.userArea = checkedList

        (activity as SignupActivity)!!.welcomeActivity()
        return
    }

    private fun initView(view:View) {
        tlName = view.findViewById<TextInputLayout>(R.id.tl_name)
        tlEmail = view.findViewById<TextInputLayout>(R.id.tl_email)
        tlSchool = view.findViewById<TextInputLayout>(R.id.tl_school)
        tlMajor = view.findViewById<TextInputLayout>(R.id.tl_major)
        tlGrade = view.findViewById<TextInputLayout>(R.id.tl_grade)

        etName = view.findViewById<TextInputEditText>(R.id.et_name)
        etEmail = view.findViewById<TextInputEditText>(R.id.et_email)
        etSchool = view.findViewById<TextInputEditText>(R.id.et_school)
        etMajor = view.findViewById<TextInputEditText>(R.id.et_major)
        ddGrade = view.findViewById<AutoCompleteTextView>(R.id.dd_grade)
        btnArea = view.findViewById<AppCompatImageButton>(R.id.btn_area)
        btnOk = view.findViewById<AppCompatButton>(R.id.btn_ok)

        etName.setText(viewModel.userName)
        etEmail.setText(viewModel.userEmail)
        btnOk.isEnabled = true

        selectedArea1 = view.findViewById<TextView>(R.id.tv_select1)
        selectedArea2 = view.findViewById<TextView>(R.id.tv_select2)
        selectedArea3 = view.findViewById<TextView>(R.id.tv_select3)
        selectedArea1.visibility = View.GONE
        selectedArea2.visibility = View.GONE
        selectedArea3.visibility = View.GONE
    }

    private fun setDropdown() {
        val gradeAdapter = ArrayAdapter(requireContext(),
            R.layout.grade_dropdown_item, gradeArrayList)
        ddGrade.setDropDownBackgroundResource(R.color.background)
        ddGrade.setAdapter(gradeAdapter)
    }

    private fun validInput(et:TextInputEditText, tl:TextInputLayout, matcher: Pattern, error:String): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && et.text.toString() // 값이 있고 무효할 때
                    .isNotEmpty() && !matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = error
            } else if (!hasFocus && et.text.toString() // 값이 있고 유효할 때
                    .isNotEmpty() && matcher.matcher(et.text.toString()).find()
            ) {
                tl.error = null
            } else if (!hasFocus && et.text.toString().isEmpty() // 값이 없을 때
            ) {
                tl.error = null
            }
        }
    }

    private fun showDialog(context: Context?): AlertDialog {
        val mBuilder = AlertDialog.Builder(this.context, R.style.DetailDialog).setView(mDialogView)
        return mBuilder.show()
    }
}


fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

private fun AppCompatToggleButton.setTitle(title: String) {
    this.text = title
    this.textOn = title
    this.textOff = title
}
