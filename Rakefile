require 'rubygems'
require 'rake'

def java_files
  @java_files = Dir['src/**/**/**/**/**/*.java'].join(' ')
end

task :default => [:build]

task :compile do
  sh "mkdir -p build/"
  exec "javac #{java_files} -d build"
end

task :build do
  exec "jar -cf orman.jar -C build ."
end
