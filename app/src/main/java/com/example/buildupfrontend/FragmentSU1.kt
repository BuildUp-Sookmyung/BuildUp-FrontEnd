package com.example.buildupfrontend

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class FragmentSU1: Fragment(), onBackPressedListener {

    private val viewModel : SignupViewModel by activityViewModels()

    lateinit var clAll: ConstraintLayout
    lateinit var clService: ConstraintLayout
    lateinit var clPersInfo: ConstraintLayout
    lateinit var clMarketing: ConstraintLayout
    lateinit var clSms: ConstraintLayout
    lateinit var clEmail: ConstraintLayout

    lateinit var cbAll: CheckBox
    lateinit var cbService: CheckBox
    lateinit var cbPersInfo: CheckBox
    lateinit var cbMarketing: CheckBox
    lateinit var cbSms: CheckBox
    lateinit var cbEmail: CheckBox

    lateinit var btnDetail1: Button
    lateinit var btnDetail2: Button
    lateinit var btnOpen: Button
    lateinit var btnOpenImg: ImageView
    lateinit var btnOk: AppCompatButton
    var btnOpened = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater!!.inflate(R.layout.fragment_su1, container, false)
        initView(view)

        (activity as SignupActivity?)!!.setProgressBar(1)
        controlAllCb() // 전체동의 버튼 관리
        controlMarketingCb() // 마케팅 버튼 관리

        // 이용약관 자세히 보기: Dialog
        btnDetail1.setOnClickListener {
            val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_detail1, null)
            val mBuilder = AlertDialog.Builder(this.context, R.style.DetailDialog).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val tvContent = mDialogView.findViewById<TextView>(R.id.tv_content)
            val btnClose = mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
            tvContent.text = "※ 본 약관은 2022년 2월 7일부터 시행됩니다.\n" +
                    "\n" +
                    "제 1조 (목적)\n" +
                    "본 약관은 (주)빌드업(이하 “회사”)이 제공하는 제반 서비스의 이용과 관련하여, 회사와 회원과의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.\n" +
                    "\n" +
                    "제 2조 (정의)\n" +
                    "본 약관에서 사용하는 용어의 정의는 아래 각 호와 같습니다.\n" +
                    "\n" +
                    "1. 서비스란, 이용자가 사용하는 단말기(PC, TV, 휴대형단말기 등 각종 유무선 장치를 포함)와 상관없이 회사가 운영하는 빌드업 어플을 통해 제공하는 일체의 서비스를 의미\n" +
                    "2. 이용자란, 회사의 홈페이지 등에 접속하여 본 약관에 따라 회사가 제공하는 서비스를 받는 회원 및 비회원을 의미\n" +
                    "3. 회원이란, 본 약관에 따라 회사와 이용계약을 체결하고 회사가 제공하는 서비스를 이용하는 고객을 의미\n" +
                    "4. 비회원이란, 본 약관에 따라 회사와 이용계약을 체결하지 않고, 회사가 제공하는 서비스를 이용하는 고객을 의미, 회사가 제공하는 서비스 중 일부는 비회원의 경우에도 이용할 수 있으나, 일부 콘텐츠에 대해서는 이용이 제한될 수 있음 또한 비회원은 원칙적으로 본 약관의 적용을 받지 않지만, 관련 법령에 근거하여 금지되는 행위에 대해서는 제재를 받을 수 있음\n" +
                    "5. 아이디(ID)란, 회원의 식별과 서비스 이용을 위하여 회원이 정하고 회사가 승인하는 전자우편 주소 등을 의미\n" +
                    "6. 비밀번호란, 회원이 부여받은 아이디와 일치되는 회원임을 확인하고 비밀의 보호를 위해 회원 자신이 정한 문자 또는 숫자의 조합을 의미"

            btnClose.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        btnDetail2.setOnClickListener {
            val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_detail2, null)
            val mBuilder = AlertDialog.Builder(this.context, R.style.DetailDialog).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val tvContent1 = mDialogView.findViewById<TextView>(R.id.tv_content1)
            val tvContent2 = mDialogView.findViewById<TextView>(R.id.tv_content2)
            val btnClose = mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
            tvContent1.text = "㈜빌드업(이하 회사)은 빌드업 어플 이용을 위해 필요한 최소한의 범위로 개인정보를 수집합니다. " +
                    "또한, 제공하는 서비스(취업 정보제공 등) 특성상 「근로기준법」에 따라 만 15세 미만인 경우 회원가입을 허용하지 않습니다. " +
                    "회사에서 제공하는 서비스 유형에 따라 다음과 같이 개인정보를 수집, 이용, 보유 및 파기하고 있습니다."
            tvContent2.text = "1) 회사는 법령에 따른 개인정보 보유∙이용기간 또는 이용자로부터 개인정보를 수집 시에 동의 받은 개인정보 보유∙이용기간 내에서 개인정보를 처리하며, " +
                    "개인정보의 처리 목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다.\n" +
                    "2) 개인정보 처리 및 보유 기간은 다음과 같으며, 아래 각 항목에 대하여 이용자가 명시적으로 파기 요청을 하는 경우 지체 없이 파기합니다.\n" +
                    "- 회원 탈퇴 및 서비스 종료 시 즉시 파기\n" +
                    "- 회원이 개인정보에 대한 권리 행사를 통해 수"


            btnClose.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        // 마케팅 수신동의 자세히
        btnOpen.setOnClickListener {
            openButton()
        }

        // 다음 단계로 이동
        btnOk.setOnClickListener { nextStep() }

        return view
    }

    private fun initView(view: View) {
        clAll = view.findViewById<ConstraintLayout>(R.id.cl_all)
        clService = view.findViewById<ConstraintLayout>(R.id.cl_service)
        clPersInfo = view.findViewById<ConstraintLayout>(R.id.cl_persInfo)
        clMarketing = view.findViewById<ConstraintLayout>(R.id.cl_marketing)
        clSms = view.findViewById<ConstraintLayout>(R.id.cl_sms)
        clEmail = view.findViewById<ConstraintLayout>(R.id.cl_email)
        cbAll = view.findViewById<CheckBox>(R.id.cb_all)
        cbService = view.findViewById<CheckBox>(R.id.cb_service)
        cbPersInfo = view.findViewById<CheckBox>(R.id.cb_persInfo)
        cbMarketing = view.findViewById<CheckBox>(R.id.cb_marketing)
        cbSms = view.findViewById<CheckBox>(R.id.cb_sms)
        cbEmail = view.findViewById<CheckBox>(R.id.cb_email)
        btnDetail1 = view.findViewById<Button>(R.id.btn_detail1)
        btnDetail2 = view.findViewById<Button>(R.id.btn_detail2)
        btnOpen = view.findViewById<Button>(R.id.btn_open)
        btnOpenImg = view.findViewById<ImageView>(R.id.btn_open_img)
        btnOk = view.findViewById<AppCompatButton>(R.id.btn_ok)

        // 초기 설정
        cbAll.isChecked = viewModel.checkAll
        cbService.isChecked = viewModel.checkService
        cbPersInfo.isChecked = viewModel.checkPersInfo
        cbMarketing.isChecked = viewModel.checkMarketing
        cbSms.isChecked = viewModel.checkSms
        cbEmail.isChecked = viewModel.checkEmail

        clSms.visibility = View.GONE
        clEmail.visibility = View.GONE
        controlOkBtn()
    }

    private fun controlAllCb() {
        cbAll.setOnClickListener { // 모든 약관 사항
            if (!cbAll.isChecked) {
                cbService.isChecked = false
                cbPersInfo.isChecked = false
                cbMarketing.isChecked = false
                cbSms.isChecked = false
                cbEmail.isChecked = false
            } else {
                cbService.isChecked = true
                cbPersInfo.isChecked = true
                cbMarketing.isChecked = true
                cbSms.isChecked = true
                cbEmail.isChecked = true
            }
            controlOkBtn()
        }

        cbService.setOnClickListener { controlOkBtn() }
        cbPersInfo.setOnClickListener { controlOkBtn() }
    }

    private fun controlOkBtn() {
        btnOk.isEnabled = cbService.isChecked && cbPersInfo.isChecked
    }

    private fun controlMarketingCb() {
        cbMarketing.setOnClickListener { // 마케팅 수신 동의
            if (!cbMarketing.isChecked) {
                cbSms.isChecked = false
                cbEmail.isChecked = false
            } else {
                cbSms.isChecked = true
                cbEmail.isChecked = true
            }
        }
        cbSms.setOnClickListener {
            if (cbMarketing.isChecked && (!cbSms.isChecked || !cbEmail.isChecked)) { // 마케팅 체크된 상태에서 sms/email 중 하나 해제
                cbMarketing.isChecked = false
            } else if (!cbMarketing.isChecked && cbSms.isChecked && cbEmail.isChecked) { // 마케팅 해제된 상태에서 sms/email 둘 다 선택
                cbMarketing.isChecked = true
            }
        }
        cbEmail.setOnClickListener {
            if (cbMarketing.isChecked && (!cbSms.isChecked || !cbEmail.isChecked)) { // 마케팅 체크된 상태에서 sms/email 중 하나 해제
                cbMarketing.isChecked = false
            } else if (!cbMarketing.isChecked && cbSms.isChecked && cbEmail.isChecked) { // 마케팅 해제된 상태에서 sms/email 둘 다 선택
                cbMarketing.isChecked = true
            }
        }

        openButton()
    }

    private fun openButton() {
        if (!btnOpened) {
            clSms.visibility = View.VISIBLE
            clEmail.visibility = View.VISIBLE
            btnOpenImg.setBackgroundResource(R.drawable.selector_btn_expander_close)
            btnOpened = true
        }
        else {
            clSms.visibility = View.GONE
            clEmail.visibility = View.GONE
            btnOpenImg.setBackgroundResource(R.drawable.selector_btn_expander_open)
            btnOpened = false
        }
    }

    override fun onBackPressed() {
        (activity as SignupActivity?)!!.nextFragment(0, FragmentSU0())
    }

    private fun nextStep() {
        viewModel.checkAll = cbAll.isChecked
        viewModel.checkService = cbService.isChecked
        viewModel.checkPersInfo = cbPersInfo.isChecked
        viewModel.checkMarketing = cbMarketing.isChecked
        viewModel.checkSms = cbSms.isChecked
        viewModel.checkEmail = cbEmail.isChecked
        (activity as SignupActivity?)!!.nextFragment(2, FragmentSU2())
    }
}