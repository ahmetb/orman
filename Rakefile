# ORMAN Rakefile
# ==============
#
# Author: Berker Peksag
# Author: Ahmet Alp Balkan

require 'rubygems'
require 'rake'

def java_files
  @java_files = Dir['src/**/**/**/**/**/*.java'].join(' ')
end

def jar_files
  @jar_files = Dir['lib/*.jar'].join(':')
end

task :default => [:build]

task :build => :compile do
  puts 'Packaging JAR...'  
  sh "jar -cf orman.jar -C build ."
  sh "rm -rf build/"
  puts 'Done.'
end

task :compile do
  puts 'Compiling...'  
  sh "mkdir -p build/"
  sh "javac -d build -classpath #{jar_files} #{java_files}"
  puts 'Done.'
end

