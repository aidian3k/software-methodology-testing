#!/bin/bash

file_path="application.properties"
current_version=$(grep "project.version=" "$file_path" | cut -d'=' -f2)
new_version=$(awk -F. -v OFS=. '{print $1,$2,$3+1}' <<< "$current_version")
sed -i "" "s/project.version=.*/project.version=$new_version/" "$file_path"

echo "Version upgraded to $new_version"

