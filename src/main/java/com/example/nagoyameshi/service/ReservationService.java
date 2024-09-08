package com.example.nagoyameshi.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, StoreRepository storeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 予約日時が適切かどうかをチェックするメソッド
     * @param store 店舗情報
     * @param reservationDatetime 予約日時
     * @return エラーメッセージ、適切であればnull
     */
    public String isReservationDateTimeValid(Store store, LocalDateTime reservationDatetime) {
    	
    	// 定休日の文字列からリストを作成
    	List<String> closedDays = Arrays.asList(store.getClosedDays().split(","));
        
        // 現在日時より過去の日時を選択した場合、無効
        if (reservationDatetime.isBefore(LocalDateTime.now())) {
            return "指定した日時は過去の日時です。";
        }

        // 営業終了一時間前以内の予約は無効
        LocalDateTime closingTimeWithMargin = reservationDatetime.toLocalDate().atTime(store.getClosingTime().minusHours(1));
        if (reservationDatetime.isAfter(closingTimeWithMargin)) {
            return "営業終了一時間前は指定できません。";
        }
        
     // 定休日に予約した場合、無効
        DayOfWeek reservationDay = reservationDatetime.getDayOfWeek();
        if (closedDays.contains(reservationDay.name())) {
        return "定休日は指定できません。";
        }

        // エラーがない場合は null を返す
        return null;
    }

    /**
     * 予約のバリデーションを行うメソッド
     * @param form 予約フォーム
     * @return エラーメッセージ、成功したらnull
     */
    public String validateReservation(ReservationInputForm form) {
        // storeIdからStoreを取得
        Store store = storeRepository.findById(form.getStoreId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + form.getStoreId()));

        // 予約日時が現在日時より後であることをチェック
        if (form.getReservationDatetime().isBefore(LocalDateTime.now())) {
            return "指定した日時は無効です。"; // 無効な予約
        }

        // 営業終了時間や定休日などのバリデーションロジックを追加
        String errorMessage = isReservationDateTimeValid(store, form.getReservationDatetime());
        if (errorMessage != null) {
            return errorMessage;
        }

        return null; // 有効な予約
    }

    /**
     * 予約情報を作成して保存するメソッド
     * @param reservation Reservationオブジェクト
     * @param memberId 会員ID
     * @param storeId 店舗ID
     */
    public void create(Reservation reservation, Integer memberId, Long storeId) {
        // memberIdからMemberを取得
        Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        // storeIdからStoreを取得
        Store store = storeRepository.findById(storeId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeId));

        // ReservationにMemberとStoreを設定
        reservation.setMember(member);
        reservation.setStore(store);

        // その他の設定が必要であれば設定
        reservationRepository.save(reservation);
    }
}
