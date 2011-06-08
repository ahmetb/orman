# ORMAN Rakefile
# ==============
#
# Author: berker peksag
# Author: ahmet alp balkan

require 'rubygems'
require 'rake'

def java_files
  @java_files = Dir['src/**/**/**/**/**/*.java'].join(' ')
end

def jar_files
  @jar_files = Dir['lib/**.jar'].join(':')
end

task :default => [:build]

task :build => :compile do
  print "Packaging JAR...\n"  
  sh "jar -cf orman.jar -C build ."
  sh "rm -rf build/"
  print "Done.\n"
end

task :compile do
  print "Compiling...\n"  
  sh "mkdir -p build/"
  sh "javac -d build -classpath #{jar_files} #{java_files}"
end

