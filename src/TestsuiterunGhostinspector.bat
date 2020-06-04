@echo off


echo "Now Getting the Test Suite and Running The Test suite"
newman run "src/Ghost_Inspector_Dorsa_Testing_Suite.postman_collection.json" -r htmlextra,html -e "src/Global_Ghost_Inspector.postman_environment.json"
