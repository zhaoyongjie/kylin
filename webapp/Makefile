extract:
	pybabel extract -F translations/babel.cfg --keywords=__ -o translations/messages.pot .

update:
	pybabel update -i translations/messages.pot -d translations/ -l zh

# compile:
# 	pybabel compile -f -d translations/ -l zh

tojson:
	po2json -d kylin -f jed1.x translations/zh/LC_MESSAGES/messages.po translations/zh/LC_MESSAGES/messages.json
