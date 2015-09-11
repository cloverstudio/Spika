var browserify = require('browserify'),
    watchify = require('watchify'),
    gulp = require('gulp'),
    plumber = require('gulp-plumber'),
    gutil = require('gulp-util'),
    source = require('vinyl-source-stream'),
    sass = require('gulp-sass'),
    hbsfy = require("hbsfy"),
    sourceFile = './src/client/js/main.js',
    destFolder = './public/js/',
    destFile = 'bundle.js',
    sourceCSS = 'src/client/css/',
    destCSS = 'public/css/',
    apidoc = require('gulp-apidoc'),
    fs = require('fs-extra'),
    jsdoc = require("gulp-jsdoc");

var props = {
    entries: sourceFile,
    debug: true,
    cache: {},
    packageCache: {},
};

var infos = {
	name: "Spika Messenger",
	version: "0.0.1"
};

// build for dist
gulp.task('browserify-build', function() {

    var bundler = browserify({
        // Required watchify args
        cache: {}, 
        packageCache: {}, 
        fullPaths: true,
        // Browserify Options
        entries: sourceFile,
        debug: true
    });
    
    hbsfy.configure({
        extensions: ['hbs']
    });
    
    var bundle = function() {
        return bundler
        .transform(hbsfy)
        .bundle()
        .on('error', function(err){
            console.log(err.message);
            this.emit('end');
        })
        .pipe(source(destFile))
        .pipe(gulp.dest(destFolder));
    };

    return bundle();
  
});

gulp.task('copy', function() {

    fs.mkdirsSync('public/uploads')
    gulp.src('src/client/js/adapter.js').pipe( gulp.dest('public/js') );
    gulp.src('src/client/*.html').pipe( gulp.dest('public') );
    gulp.src('src/client/img/**/*').pipe( gulp.dest('public/img') );
    gulp.src('node_modules/bootstrap-sass/assets/fonts/**/*').pipe( gulp.dest('public/fonts') );
    gulp.src('src/client/css/backgroundsize.min.htc').pipe( gulp.dest('public') );
    gulp.src('node_modules/jquery-colorbox/example1/images/*').pipe( gulp.dest('public/css/images') );

});

gulp.task('build-css', function() {

  return gulp.src(sourceCSS + '*.scss')
    .pipe(plumber())
    .pipe(sass())
    .pipe(gulp.dest(destCSS));
});

gulp.task('build-apidoc', function(){

    apidoc.exec({
        src: "src/",
        dest: "doc/API"
    });

});

var JSDocTemplate = {
	path: "ink-docstrap",
    systemName: "Spika Web Client",
    theme: "cosmo",
    linenums: true
};

var JSDocOptions = {
    outputSourceFiles: true
};

gulp.task("jsdoc", function() {
	gulp.src(["./src/client/**/*.js", "README.md"])
		.pipe(jsdoc.parser(infos))
		.pipe(jsdoc.generator("./doc/WebClient", JSDocTemplate, JSDocOptions))
});


gulp.task('build-dist',['browserify-build','build-css','build-apidoc','copy'],function(){

    
});

gulp.task('dev-all',['copy','browserify-build','build-css','build-apidoc'],function(){
    
    gulp.watch("./src/client/**/*.js", ["jsdoc"])
    gulp.watch('src/client/**/*',['build-dist']);
    gulp.watch('src/server/**/*',['build-dist']);
    
});

gulp.task('build-dev-fast',['browserify-build'],function(){
    
    gulp.watch('src/client/**/*',['build-dist']);
    gulp.watch('src/server/**/*',['build-dist']);
    
});

gulp.task('default',['dev-all'],function(){
    
    
});
