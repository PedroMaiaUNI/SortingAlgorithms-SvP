link do github: https://github.com/PedroMaiaUNI/SortingAlgorithms-SvP

# **Análise de Desempenho de Algoritmos de Busca em Ambientes Concorrentes e Paralelos: Um Estudo Comparativo em Java**

**Autor 1:** Pedro Lucas Maia de Lima 

**Palavras-chave:** ordenação. desempenho. paralelismo. multithreading. algoritmos.

---

## **RESUMO**

Este trabalho propõe uma análise detalhada do desempenho de diferentes algoritmos de busca em ambientes seriais e paralelos, utilizando a linguagem de programação Java. A busca por eficiência computacional é essencial em diversas aplicações, e entender como diferentes algoritmos se comportam em diferentes cenários de processamento é de suma importância. Neste estudo, serão abordados quatro algoritmos de busca populares, tanto em suas implementações sequenciais quanto em versões paralelizadas. Serão realizadas análises comparativas utilizando uma variedade de conjuntos de dados de entrada e ajustando o número de núcleos de processamento. Os resultados serão registrados em arquivos CSV, permitindo uma análise visual através de gráficos ou processamento adicional utilizando Java.

---

## **INTRODUÇÃO**

Com o avanço da computação paralela e o aumento do número de núcleos em processadores modernos, torna-se essencial compreender como algoritmos clássicos de ordenação se comportam em ambientes multi-thread. 
Este estudo investiga o impacto da paralelização sobre o tempo de execução de algoritmos amplamente conhecidos, comparando implementações seriais e paralelas para diferentes quantidades de dados.  
A motivação principal é avaliar se a paralelização realmente proporciona ganhos de desempenho ou, em certos casos, acarreta sobrecarga que compromete a eficiência.  

---

## **METODOLOGIA**

Implementação dos algoritmos de busca em Java, tanto em versões sequenciais quanto paralelas, utilizando as melhores práticas de programação concorrente.
Desenvolvimento de um "framework de teste" que permita variar o tamanho e a complexidade dos conjuntos de dados de entrada.
A ideia é que cada método possa ser implementado com um algoritmo serial e paralelo de ordenação.
Execução dos testes em diferentes ambientes de processamento, variando o número de núcleos disponíveis.
Registro dos tempos de execução e outros parâmetros relevantes em arquivos CSV.
Análise estatística dos resultados obtidos para identificar padrões de desempenho e comparar os algoritmos sob diferentes condições.

---

## **RESULTADOS E DISCUSSÃO**

Os resultados parciais demonstram comportamentos distintos entre os algoritmos analisados.  

- **Bubble Sort:** apresentou tempos de execução extremamente altos à medida que o tamanho do vetor aumentou. Em arrays de 1.000.000 elementos, a versão serial ultrapassou 1.000.000 ms (aproximadamente 17 minutos), enquanto as versões paralelas chegaram a superar 3.000.000 ms (cerca de 50 minutos). Isso indica que a sobrecarga de sincronização e divisão de tarefas torna a paralelização ineficiente para algoritmos com complexidade O(n²). O algoritmo de paralelização para esse caso é popularmente conhecido como Odd-Even Sorting, pois consiste em percorrer de par em par pelo array fazendo comparações entre os pares a cada ciclo, alternando entre (par, ímpar) e (ímpar, par).

- **Insertion Sort:** teve comportamento semelhante ao *Bubble Sort*, com tempos crescentes e perda de desempenho nas versões paralelas. A forma como esse algoritmo foi paralelizado foi improvisada e pode ter prejudicado nesse aspecto. Consistiu em tentar paralelizar a busca pelo índice de inserção.

- **Merge Sort:** inicialmente sofreu com os problemas de overhead já observados, mas conforme o avanço do teste apresentou melhoria progressiva com o aumento do número de threads, especialmente a partir de 10.000 elementos, demonstrando boa escalabilidade e aproveitamento do paralelismo devido à sua estrutura recursiva e divisões equilibradas.  

- **Quick Sort:** mostrou-se o algoritmo com melhor desempenho geral, tanto em versão serial quanto paralela. Para grandes volumes de dados, a versão paralela com 8 threads reduziu o tempo de execução em até 70% em relação à serial.  

De forma geral, observa-se que nem todo algoritmo se beneficia da paralelização — o custo de coordenação entre threads pode anular os ganhos esperados, sobretudo em algoritmos com alta dependência sequencial.

---

## **CONCLUSÃO**

A análise comparativa evidenciou que a eficiência da paralelização depende fortemente da natureza do algoritmo e da quantidade de dados processados.  
Enquanto *Merge Sort* e *Quick Sort* mostraram bom aproveitamento dos recursos paralelos, *Bubble Sort* e *Insertion Sort* sofreram degradação de desempenho devido ao aumento da complexidade e ao overhead de sincronização.  
Os resultados reforçam a importância de selecionar algoritmos adequados ao contexto de execução e de avaliar criteriosamente os custos da paralelização antes de sua aplicação prática.  
Vale apontar que, apesar de propôr uma melhora de desempenho significativa, a paralelização em GPU teoricamente constataria os mesmos resultados comparativos. 

<!---
---

## **REFERÊNCIAS**

SCANDURA, Leonardo Felix; CURVÊLLO, Rodrigo. ESTUDO DE CASO: DESEMPENHO NA ORDENAÇÃO DE CONJUNTO DE DADOS-SERIAL X PARALELO. Anais da Feira do Conhecimento Tecnológico e Científico, n. 25, 2024.

GEEKSFORGEEKS. **Quick Sort Algorithm**. Disponível em: https://www.geeksforgeeks.org/quick-sort-algorithm/. Acesso em: 25 out. 2025

GEEKSFORGEEKS. **MergeSort**. Disponível em: https://www.geeksforgeeks.org/merge-sort/. Acesso em: 25 out. 2025

BRO CODE. **Learn Insertion Sort in 7 minutes**. Disponível em: https://youtu.be/8mJ-OhcfpYg?si=7F2X0Jp8Hxw-l0oM. Acesso em: 31 out. 2025.

WIKIPEDIA CONTRIBUTORS. **Odd–even sort**. Disponível em: https://en.wikipedia.org/wiki/Odd%E2%80%93even_sort.
-->