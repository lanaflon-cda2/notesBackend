var gulp = require('gulp');
var uglify = require('gulp-uglify');
var minify = require('gulp-minify');
var concat = require('gulp-concat');
var cssmin = require('gulp-cssmin');
var ngAnnotate = require('gulp-ng-annotate');
var htmlsrc = require('gulp-html-src');
var pump = require('pump');

gulp.task('styles', function(cb){
    pump([
        gulp.src('./css/*.css'),
        cssmin(),
        concat('styles.min.css'),
        gulp.dest('./../resources/static/css/')
    ], cb);
});

gulp.task('scripts', function(fo){
    pump([
            gulp.src('./index.html'),
            htmlsrc(),
            ngAnnotate(),
            uglify(),
            concat('app.min.js'),
            gulp.dest('./../resources/static/js/')
        ], fo);
});

gulp.task('default', ['styles', 'scripts'], function(){});