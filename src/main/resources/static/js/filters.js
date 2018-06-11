angular.module("notesApp.filters", []).filter('startFrom', function () {
    return function (input, start) {
        return (input === undefined) ? [] : input.slice(start);
    };
}).filter('offset', function () {
    return function (input, start) {
        start = parseInt(start, 10);
        return (input === undefined) ? [] : input.slice(start);
    };
});