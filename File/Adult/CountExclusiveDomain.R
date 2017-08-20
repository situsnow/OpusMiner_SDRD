
dim(filter(Adult_R,
           field3 == "Masters",
           field6 == "Exec-managerial",
           #field7 == "Husband",
           #field11 == "United-States",
           field12 == "2",
           field10 != "Hour51-70"
           ))[1]

dim(filter(Adult_R,
           field10 != "Hour51-70"
))[1]

