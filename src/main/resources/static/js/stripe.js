document.getElementById('payment-button').addEventListener('click', function () {
    // CSRFトークンをmetaタグから取得
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/auth/paid_signup/session', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken // CSRFトークンをリクエストヘッダーに追加
        },
        body: JSON.stringify({}),
    })
    .then(function (response) {
        return response.json();  // ここでJSONとしてパース
    })
    .then(function (data) {
        if (data.url) {
            window.location.href = data.url; // セッションURLにリダイレクト
        } else if (data.error) {
            alert(data.error); // エラーメッセージを表示
        } else {
            alert('支払い登録中にエラーが発生しました。');
        }
    })
    .catch(function (error) {
        console.error('Error:', error);
        alert('通信エラーが発生しました。');
    });
});
