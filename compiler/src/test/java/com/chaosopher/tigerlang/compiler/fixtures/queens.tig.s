.global tigermain
.data
L10:
	.long  0x2
	.ascii " O\0"
.data
L11:
	.long  0x2
	.ascii " .\0"
.data
L15:
	.long  0x2
	.ascii "\n\0"
.data
L16:
	.long  0x2
	.ascii "\n\0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# start 
L34:
movq %rdi, -8(%rbp) # store to offset 1
movq %r12, -40(%rbp) # spill store
movq %r13, -32(%rbp) # spill store
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %r12 # move(t, i)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %r14 # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %r14
L2:
cmp %r14, %r12
jle L3
L5:
movq $L16, %rdi # default name
call print
movq -40(%rbp), %r12 # spill load
movq -32(%rbp), %r13 # spill load
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L33
L3:
movq $0, %r13 # move(t, i)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %r15 # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %r15
L6:
cmp %r15, %r13
jle L7
L9:
movq $L15, %rdi # default name
call print
cmp %r14, %r12
je L5
L4:
movq $1, %rax # bin(t, i)
add %rax, %r12
jmp L2
L7:
movq -8(%rbp), %rax # mem(boe)
movq -24(%rax), %rcx # mem(boe)
movq $8, %rdx # bin(t, i)
movq %r12, %rax # imul l -> rax
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rax # mem(temp)
cmp %r13, %rax
je L12
L13:
movq $L11, %rdi # default name
L14:
call print
cmp %r15, %r13
je L9
L8:
movq $1, %rax # bin(t, i)
add %rax, %r13
jmp L6
L12:
movq $L10, %rdi # default name
jmp L14
L33:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
# start main
# start 
L36:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -32(%rbp) # spill store
movq %r13, -24(%rbp) # spill store
movq %r14, -16(%rbp) # spill store
movq -8(%rbp), %rcx # mem(boe)
movq -8(%rcx), %rcx # mem(boe)
movq -32(%rbp), %rsi # spill load
cmp %rcx, %rsi
je L30
L31:
movq $0, %r13 # move(t, i)
movq -8(%rbp), %rcx # mem(boe)
movq -8(%rcx), %r14 # mem(boe)
movq $1, %rcx # bin(t, i)
sub %rcx, %r14
L17:
cmp %r14, %r13
jle L18
L20:
L32:
movq -24(%rbp), %r13 # spill load
movq -16(%rbp), %r14 # spill load
jmp L35
L30:
movq -8(%rbp), %rdi # mem(boe)
call L0
jmp L32
L18:
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rcx # mem(boe)
movq $8, %rdx # bin(t, i)
movq %r13, %rax # imul l -> rax
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rcx # mem(temp)
movq $0, %rdx # integerExpression
cmp %rdx, %rcx
je L21
L22:
L25:
L28:
L29:
cmp %r14, %r13
je L20
L19:
movq $1, %rcx # bin(t, i)
add %rcx, %r13
jmp L17
L21:
movq -8(%rbp), %rax # mem(boe)
movq -32(%rax), %rcx # mem(boe)
movq %r13, %rax # add lexp -> r
movq -32(%rbp), %rsi # spill load
add %rsi, %rax
movq $8, %rdx # bin(t, i)
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rcx # mem(temp)
movq $0, %rdx # integerExpression
cmp %rdx, %rcx
jne L25
L24:
movq -8(%rbp), %rax # mem(boe)
movq -40(%rax), %rcx # mem(boe)
movq $7, %rdx # bin(t, i)
movq %r13, %rax # add lexp -> r
add %rdx, %rax
movq -32(%rbp), %rsi # spill load
sub %rsi, %rax
movq $8, %rdx # bin(t, i)
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rcx # mem(temp)
movq $0, %rdx # integerExpression
cmp %rdx, %rcx
jne L28
L27:
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rax # mem(boe)
movq $1, %rcx # integerExpression
movq %rcx, (%rax, %r13, 8) # store array
movq -8(%rbp), %rax # mem(boe)
movq -32(%rax), %rcx # mem(boe)
movq %r13, %rax # add lexp -> r
movq -32(%rbp), %rsi # spill load
add %rsi, %rax
movq $1, %rdx # integerExpression
movq %rdx, (%rcx, %rax, 8) # store array
movq -8(%rbp), %rax # mem(boe)
movq -40(%rax), %rcx # mem(boe)
movq $7, %rdx # bin(t, i)
movq %r13, %rax # add lexp -> r
add %rdx, %rax
movq -32(%rbp), %rsi # spill load
sub %rsi, %rax
movq $1, %rdx # integerExpression
movq %rdx, (%rcx, %rax, 8) # store array
movq -8(%rbp), %rax # mem(boe)
movq -24(%rax), %rax # mem(boe)
movq -32(%rbp), %rsi # spill load
movq %r13, (%rax, %rsi, 8) # store array
movq -8(%rbp), %rdi # mem(boe)
movq $1, %rax # bin(t, i)
movq -32(%rbp), %rsi # spill load
add %rax, %rsi
call L1
movq -8(%rbp), %rcx # mem(boe)
movq -16(%rcx), %rcx # mem(boe)
movq $0, %rdx # integerExpression
movq %rdx, (%rcx, %r13, 8) # store array
movq -8(%rbp), %rcx # mem(boe)
movq -32(%rcx), %rdx # mem(boe)
movq %r13, %rcx # add lexp -> r
movq -32(%rbp), %rsi # spill load
add %rsi, %rcx
movq $0, %rsi # integerExpression
movq %rsi, (%rdx, %rcx, 8) # store array
movq -8(%rbp), %rcx # mem(boe)
movq -40(%rcx), %rdx # mem(boe)
movq $7, %rsi # bin(t, i)
movq %r13, %rcx # add lexp -> r
add %rsi, %rcx
movq -32(%rbp), %rsi # spill load
sub %rsi, %rcx
movq $0, %rsi # integerExpression
movq %rsi, (%rdx, %rcx, 8) # store array
jmp L29
L35:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
# start main
# start 
L38:
movq %r15, -48(%rbp) # spill store
movq $8, -8(%rbp) # store to offset 2
movq $8, -8(%rbp) # store to offset 2
movq $-16, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
movq $0, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq $-24, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
movq $0, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq $-32, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
movq -8(%rbp), %rax # mem(boe)
add %rax, %rdi
movq $1, %rax # bin(t, i)
sub %rax, %rdi
movq $0, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq $-40, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
movq -8(%rbp), %rax # mem(boe)
add %rax, %rdi
movq $1, %rax # bin(t, i)
sub %rax, %rdi
movq $0, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq $0, %rsi # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
call L1
movq -48(%rbp), %r15 # spill load
jmp L37
L37:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
