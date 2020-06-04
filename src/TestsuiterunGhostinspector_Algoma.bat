@echo off


echo "Now Getting the Test Suite and Running The Test suite"
newman run "src/GHOSTINSPECTOR_ALGOMA_UNIVERSITY.postman_collection.json" -r htmlextra,html -e "src/ALGOMA_UNIVERISTY_VARIABLES.postman_environment.json"
