package com.example.blackstone.shopping

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.google.android.material.snackbar.Snackbar

class ShoppingFragment : Fragment() {

    private var currency = 1450
    private lateinit var tvCurrency: TextView
    private lateinit var tvBuy: TextView
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_shopping, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = view

        tvCurrency = view.findViewById(R.id.tvCurrency)
        tvBuy = view.findViewById(R.id.tvBuy)

        updateCurrency()

        tvBuy.setOnClickListener { showPurchaseDialog() }
    }

    private fun updateCurrency() {
        tvCurrency.text = "%,d".format(currency)
    }


    private fun showPurchaseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_purchase_confirm, null)
        dialogView.findViewById<TextView>(R.id.tvMessage).text =
            "정말 위너 쉴드를 구매하시겠어요?\n구매 후에는 취소할 수 없습니다."
        val dialog = AlertDialog.Builder(requireContext(), R.style.WinnerFit_Dialog_Transparent)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<TextView>(R.id.btnConfirm).setOnClickListener {
            val price = 500
            if (currency < price) {
                Snackbar.make(root, "재화가 부족합니다.", Snackbar.LENGTH_SHORT).show()
            } else {
                currency -= price
                updateCurrency()
                showSuccessDialog("위너 쉴드")
            }
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showSuccessDialog(itemName: String) {
        val view = layoutInflater.inflate(R.layout.dialog_purchase_success, null)
        view.findViewById<TextView>(R.id.tvMessage).text = "${itemName}를 구매하였습니다!"

        val dialog = AlertDialog.Builder(requireContext(), R.style.WinnerFit_Dialog_Transparent)
            .setView(view)
            .create()

        view.findViewById<TextView>(R.id.btnOk).setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}