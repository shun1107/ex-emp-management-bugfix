"use strict";

$(function() {
    $("#employee-search").autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "/employee/searchAutocomplete",
                type: "GET",
                data: {
                    term: request.term
                },
                dataType: "json",
                success: function(data) {
                    response(data);
                }
            });
        },
        minLength: 2,
        messages: {
            noResults: '',
            results: function() {}
        }
    });
});