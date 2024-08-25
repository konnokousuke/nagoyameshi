document.addEventListener("DOMContentLoaded", function () {
    flatpickr("#reservationDatetime", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        minDate: "today",
    });
});
