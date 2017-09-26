#Set authentication
export BIGML_USERNAME=xsit1
export BIGML_API_KEY=f2cf03cfa47391024127f6583804eb1ddbf49286
export BIGML_AUTH="username=$BIGML_USERNAME;api_key=$BIGML_API_KEY"

#Create a source
curl "https://bigml.io/source?$BIGML_AUTH" -F file=@Adult_Header.csv

#source/59aba7e51333b32bb5005b03

#Create a dataset
curl "https://bigml.io/dataset?$BIGML_AUTH" \
    -X POST \
    -H 'content-type: application/json' \
    -d '{"source": "source/59abab232ba71578e90062f9"}'

# Create association
curl "https://bigml.io/association?$BIGML_AUTH" \
     -X POST \
     -H 'content-type: application/json' \
     -d '{"dataset": "dataset/59abab331333b32ba800316d",
     	  "rhs_predicate": "[{"field": "field12", "operator": "=", "value":">50K"}]",
     	  "max_lhs":"10",
     	  "search_strategy":"lift",
     	  "max_k":"100"}'    
     	  
     	  
curl "https://bigml.io/association?$BIGML_AUTH" \
     -X POST \
     -H 'content-type: application/json' \
     -d '{"dataset": "dataset/59abab331333b32ba800316d",
     	  "max_lhs":10,
     	  "search_strategy":"lift",
     	  "max_k":100}'
     	  
     	  
     	  association/59acf2a01333b32bb5007a4f
     	  curl "https://bigml.io/association/59acf2a01333b32bb5007a4f?$BIGML_AUTH"





