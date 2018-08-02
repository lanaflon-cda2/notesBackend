var gulp = require('gulp');
var uglify = require('gulp-uglify');
var minify = require('gulp-minify');
var concat = require('gulp-concat');
var cssmin = require('gulp-cssmin');
var htmlBeautify = require('gulp-html-beautify');
var minifyHtml = require('gulp-minify-html');
var deleteLines = require('gulp-delete-lines');
var insertLines = require('gulp-insert-lines');
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

gulp.task('index-scripts', function(fo){
    pump([
        gulp.src('./index.html'),
        htmlsrc(),
        ngAnnotate(),
        uglify(),
        concat('app.min.js'),
        gulp.dest('./../resources/static/js/')
    ], fo);
});

gulp.task('html-files', function(fo){
    pump([
        gulp.src('./modules/**/views/*.html'),
        minifyHtml({empty:true}),
        htmlBeautify(),
        gulp.dest('./../resources/static/modules/', {overwrite: true})
    ],fo);
});

gulp.task('index-file', function(fo){
    pump([
        gulp.src('./index.html'),
        deleteLines({
            'filters': [
                /<link\s+href=["']/i
            ],
        }),
        insertLines({
            'before': /<\/head>$/,
            'lineBefore': '<link rel="stylesheet" type="text/css" href="css/styles.min.css"/>'
        }),
        deleteLines({
            'filters': [
                /<script\s+src=/i
            ],
        }),
        insertLines({
            'before': /<\/body>$/,
            'lineBefore': '<script type="text/javascript" src="js/app.min.js"></script>'    
        }),
        minifyHtml({empty:true}),
        htmlBeautify(),
        gulp.dest('./../resources/static/')
    ], fo);
});

gulp.task('default', ['html-files', 'styles', 'index-scripts', 'index-file'], function(){});