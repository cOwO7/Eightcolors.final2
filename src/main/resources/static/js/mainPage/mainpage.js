document.addEventListener("DOMContentLoaded", function() {
    const checkinInput = document.getElementById('checkin');
    const checkoutInput = document.getElementById('checkout');
    const checkinCalendar = document.getElementById('checkinCalendar');
    const checkoutCalendar = document.getElementById('checkoutCalendar');

    /*const checkinPicker = flatpickr(checkinCalendar, {
        inline: true,
        onChange: function(selectedDates, dateStr, instance) {
            checkinInput.value = dateStr;
            checkoutPicker.set('minDate', dateStr);
            hideDropdown(checkinInput);
        }
    });*/

    const checkoutPicker = flatpickr(checkoutCalendar, {
        inline: true,
        onChange: function(selectedDates, dateStr, instance) {
            checkoutInput.value = dateStr;
            hideDropdown(checkoutInput);
        }
    });

    checkinInput.addEventListener('focus', function() {
        showDropdown(checkinInput);
    });

    checkoutInput.addEventListener('focus', function() {
        showDropdown(checkoutInput);
    });

    function showDropdown(input) {
        const dropdownMenu = input.nextElementSibling;
        if (dropdownMenu) {
            new bootstrap.Dropdown(input).show();
        }
    }

    function hideDropdown(input) {
        const dropdownMenu = input.nextElementSibling;
        if (dropdownMenu) {
            new bootstrap.Dropdown(input).hide();
        }
    }
});