library(bigml)

#set working directory
#setwd("~/git/OpusMiner_SDRD/File/input")

#set default credentials
#https://bigml.com/account/apikey
setCredentials("xsit1", "f2cf03cfa47391024127f6583804eb1ddbf49286")

#create source with dataset file
data.source = createSource("Adult.csv", header = FALSE)

# create dataset
data.dataset = createDataset(data.source)
