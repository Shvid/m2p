#!/usr/bin/env bash

echo "pom.version"
ver=`ruby -r rexml/document -e 'puts REXML::Document.new(File.new(ARGV.shift)).elements["/project/version"].text' pom.xml`
echo $ver
echo "Already released tags:"
git tag -l
echo "Do you want to release it? (enter something if yes)"
read answer


echo "Releasing $ver"
git tag "v$ver"
git push --tags
git checkout release
git merge origin/develop release
git push origin release
git checkout develop
