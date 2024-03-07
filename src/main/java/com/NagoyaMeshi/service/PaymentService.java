package com.NagoyaMeshi.service;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

public class PaymentService {

    public Charge chargeCreditCard(String token, long amount) throws Exception {
        // 支払いパラメータの設定
        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(amount) // 金額
                        .setCurrency("jpy") // 通貨
                        .setDescription("Example charge") // 説明
                        .setSource(token) // トークン
                        .build();

        // 支払いの実行
        return Charge.create(params);
    }
}