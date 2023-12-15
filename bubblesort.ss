      -- a program to bubblesort an integer array
      --
      --
      -- array pointer is stored in R30
0:  ADDI R30, R0, 4000
LABEL WHILESTART
      --
      -- set R1 to be our boolean (true at the start of a loop)
4:  ADDI R1, R0, 1
      --
      -- setup of the for loop
      -- load final array address (4000 + length) into R2
8:  LW R2, 0(R30)
12:  SLL R2, R2, 2
16:  ADD R2, R2, R30
      --
      -- R3 is our index variable, starting at 4004
20:  ADDI R3, R30, 4
LABEL FORSTART
      -- skip the next iteration if the index equals the array length
24:  BEQ R2, R3, FOREND
      --increment index variable
28:  ADDI R3, R3, 4
      --
      -- load array[i] & array[i-1] into R4 and R5 respectively
32:  LW R4, 0(R3)
36:  LW R5, -4(R3)
      --
      -- compare array[i] < array[i-1]
40:  SUB R6, R4, R5
      -- skip the if when array[i] >= array[i-1]
44:  BGEZ R6, ENDIF
      -- swap array[i] and array[i-1] in memory
48:  SW R5, 0(R3)
52:  SW R4, -4(R3)
      -- set our boolean (sorted?) to false
56:  ADDI R1, R0, 0
LABEL ENDIF
      --jump to the beginning of the for loop
60:  J FORSTART
LABEL FOREND
64:  BEQ R1, R0, WHILESTART
68:  HALT
