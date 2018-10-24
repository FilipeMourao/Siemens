/************************************************************************************************************************************
 1. Variables
 ************************************************************************************************************************************/
var gulp 		= require('gulp');						// Main gulp module
//var sass 		= require('gulp-ruby-sass');			// Gulp ruby sass
var sass 		= require('gulp-sass');					// Gulp libsass
var prefix 		= require('gulp-autoprefixer');			// CSS autoprefixer
var rename		= require('gulp-rename');				// Rename files
var concat		= require('gulp-concat');				// Javascript concatination
var uglify		= require('gulp-uglify');				// Javascript minification
var notify		= require('gulp-notify');				// Notifications
var plumber 	= require('gulp-plumber');				// Continue on error
var jscs 		= require('gulp-jscs');					// Javascript code style

/************************************************************************************************************************************
2. Default Task
************************************************************************************************************************************/
gulp.task('default', ['sass', 'javascript'], function() {});

/************************************************************************************************************************************
3. Watch
************************************************************************************************************************************/
gulp.task('watch', function() {
	// Watch .scss files
	gulp.watch('.assets/sass/**/*.scss', ['sass']);

  	// Watch .js files
	gulp.watch('.assets/js/**/*.js', ['javascript']);
});

/************************************************************************************************************************************
4. SASS
************************************************************************************************************************************/
gulp.task('sass', function () {
    gulp.src('.assets/sass/**/*.scss')
		.pipe(plumber())
        .pipe(sass({
        	style: 'compressed'
			//,'sourcemap=none': true
		}))
        .pipe(plumber())
        .pipe(prefix(
        		"last 3 version",
        		"> 5%",
        		"ie 8",
        		"ie 7"
        ))
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('com/css/'))
        .pipe(notify({message:'SASS finished'}));
});

/************************************************************************************************************************************
5. Javascript
************************************************************************************************************************************/
gulp.task('javascript', function(){
	return gulp.src(['.assets/js/**/*.js'])
		.pipe(concat('com/js/main.js'))
		.pipe(gulp.dest('.'))
		.pipe(rename({suffix: '.min'}))
		.pipe(plumber())
		.pipe(uglify())
		.pipe(gulp.dest('.'))
		.pipe(notify({ message: 'Javascript "General" finished' }));
});
gulp.task('jscs', function(){
	return gulp.src(['.assets/js/10*.js'])
		.pipe(jscs());
});
