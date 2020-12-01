	In main am folosit un for cu care am trecut prin toate comenzile date si am 
verificat tipul acestora. Dupa ce am aflat in ce categorie ne aflam, am folosit un
switch pentru toate cazurile din acea categorie (ex: pentru comenzile de tip command
puteam intra in favorite, view sau rating), pentru fiecare categorie am folosit si
un default pentru cazul in care apare o eroare neasteptata.
	Pentru a organiza clasele, am creat clasele Actions, Queries si 
Recommendations, care fiecare contine metode pentru fiecare categorie in parte,anume
Actions: pentru categoria commands; Queries: pentru categoria query;
Recommendations: pentru categoria recommendation.
	Metodele din fiecare clasa sunt explicate cu ajutorul javadoc-urilor