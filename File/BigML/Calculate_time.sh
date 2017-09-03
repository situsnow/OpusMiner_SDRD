begin=$(date +"%s")
for i in {1..100}
do
echo "This is testing"
done
termin=$(date +"%s")
difftimelps=$(($termin-$begin))
echo "$(($difftimelps / 60)) minutes and $(($difftimelps % 60)) seconds elapsed for Script Execution."