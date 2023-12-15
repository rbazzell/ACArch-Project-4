      -- a program to quicksort an integer array
      --
      --
      -- array pointer is stored in R30
0:  ADDI R30, R0, 4000
      --stack pointer starts @ 5000 in R29
4:  ADDI R29, R0, 5000
      --stack index pointer starts @ 5000 in R28
8:  ADDI R28, R0, 5000
      -- R27 = the first memory address of the array (p_0)
12:  ADDI R27, R30, 4
      -- R26 = the last address of the array (r_0)
16:  LW R26, 0(R30)
20:  SLL R26, R26, 2
24:  ADD R26, R26, R30
      --initial stack push (p_0, r_0) to 5000-5008
28:  SW R27, 0(R28)
32:  SW R26, 4(R28)
36:  ADDI R28, R28, 8
LABEL QUICKSORT
      --pop off the stack
40:  ADDI R28, R28, -8
      --R1 = p from stack
44:  LW R1, 0(R28)
      --R2 = r from stack
48:  LW R2, 4(R28)
      --quicksort algorithm
      --if p < r, do the following, otherwise, jump over
52:  SUB R3, R1, R2
56:  BGEZ R3, ENDIF
      -- R3 = q = partition(p, r)
      -- x = R4 = array[r]
60:  LW R4, 0(R2)
      -- R5 = i = p - 1
64:  ADDI R5, R1, -4
      -- initalize R6 = j = p for the for loop
68:  ADD R6, R1, R0
LABEL FORSTART
      -- if not j < r, jump to forend
72:  BEQ R6, R2, FOREND
      --R6 = &array[j]
      --R8 = array[j]
76:  LW R8, 0(R6)
      -- if array[j] <= x, move stuff around, otherwise go to next iteration
80:  SUB R7, R8, R4
84:  BGTZ R7, ENDFORIF
      -- increment i
88:  ADDI R5, R5, 4
      --R9 = array[i]
92:  LW R9, 0(R5)
      --swap array[i] and array[j] in memory
96:  SW R8, 0(R5)
100:  SW R9, 0(R6)
LABEL ENDFORIF
104:  ADDI R6, R6, 4
108:  J FORSTART
LABEL FOREND
      --increment i
112:  ADDI R5, R5, 4
      -- R8 = array[i]
116:  LW R8, 0(R5)
      --swap array[i] and array[r] in memory
120:  SW R4, 0(R5)
124:  SW R8, 0(R2)
      -- q = i + 1
128:  ADD R3, R5, R0
      --push quicksort(p, q-1) on stack
132:  ADDI R3, R3, -4
136:  SW R1, 0(R28)
140:  SW R3, 4(R28)
      --push quicksort(q+1, r) on stack
144:  ADDI R3, R3, 8
148:  SW R3, 8(R28)
152:  SW R2, 12(R28)
      --adjust stack index pointer
156:  ADDI R28, R28, 16
LABEL ENDIF
160:  BNE R28, R29, QUICKSORT
LABEL ENDSORT
164:  HALT
