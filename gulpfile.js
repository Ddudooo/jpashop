const gulp = require('gulp')
const postCSS = require('gulp-postcss')
const sass = require('gulp-sass')
const minify = require('gulp-csso')
sass.compiler = require('node-sass')

gulp.task('default', () =>
    gulp
    .src(['assets/scss/styles.scss'])
    .pipe(sass().on('error', sass.logError))
    .pipe(
        postCSS(
            [
                require('tailwindcss'),
                require('autoprefixer')
            ]
        )
    )
    .pipe(minify())
    .pipe(gulp.dest('src/main/resources/static/css'))
);