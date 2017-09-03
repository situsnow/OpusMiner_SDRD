#Set authentication
export BIGML_USERNAME=xsit1
export BIGML_API_KEY=f2cf03cfa47391024127f6583804eb1ddbf49286
export BIGML_AUTH="username=$BIGML_USERNAME;api_key=$BIGML_API_KEY"

#Create a source
curl "https://bigml.io/source?$BIGML_AUTH" -F file=@Adult_Header.csv

#Create a dataset
curl "https://bigml.io/dataset?$BIGML_AUTH" \
    -X POST \
    -H 'content-type: application/json' \
    -d '{"source": "source/4f52824203ce893c0a000053"}'