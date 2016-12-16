function openUrl {
    local URL=$1
    if which xdg-open > /dev/null
    then
      xdg-open $URL
    elif which gnome-open > /dev/null
    then
      gnome-open $URL
    elif which open > /dev/null
    then
      open $URL
    fi
}

./gradlew clean lint testReleaseUnitTest
lint_url=core/build/outputs/lint-results-debug.html
openUrl $lint_url

report_url=core/build/reports/tests/release/index.html
openUrl $report_url
