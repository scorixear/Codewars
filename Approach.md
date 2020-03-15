- Collect all characters in linear array and sort them
- search position of next character

## First row
1. R`Colum - WantedColumn`
2. insert by U`row` if `row` != 0
2.1 otherwise do D`1`, 2. and 3., then U`1` of the previous column

## Second Row - one before last Row
1. if cell is already in wanted row
    - D`1`
    - do step 3, 4, 5
    - then U`1`
2. if cell is already in wanted column
    - R`1`
3. D`Row - Wanted Row` of wanted column
4. R`Column - Wanted Column` of current row
5. U`Row - wanted Row` of wanted Column

## Last Row
1. R`Column` (Solve first item)
2. if Columns <= 2 - solved
3. BREAKs 
