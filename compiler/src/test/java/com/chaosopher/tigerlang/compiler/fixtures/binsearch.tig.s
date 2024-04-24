.global tigermain
.data
L4:
	.long  0x0
	.ascii "\0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L17:
movq %rdi, -8(%rbp) # store to offset 1
movq $L4, %rdi # default name
call print
jmp L16
L16:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# start 
L19:
movq %rdi, -8(%rbp) # store to offset 1
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %r14 # move(t, i)
movq -8(%rbp), %rcx # mem(boe)
movq -8(%rcx), %r15 # mem(boe)
movq $1, %rcx # bin(t, i)
sub %rcx, %r15
L5:
cmp %r15, %r14
jle L6
L8:
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L18
L6:
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rcx # mem(boe)
movq $2, %rdx # bin(t, i)
movq %r14, %rax # imul l -> rax
imul %rdx # imul rax * r 
movq $1, %rdx # bin(t, i)
add %rdx, %rax
movq %rax, (%rcx, %r14, 8) # store array
movq -8(%rbp), %rdi # mem(boe)
call L0
cmp %r15, %r14
je L8
L7:
movq $1, %rcx # bin(t, i)
add %rcx, %r14
jmp L5
L18:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L2:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L21:
movq %rdi, -8(%rbp) # store to offset 1
movq %rdx, %r8 # move(t, t)
cmp %r8, %rsi
je L12
L13:
movq %rsi, %rax # add lexp -> r
add %r8, %rax
movq $2, %rdi # bin(t, i)
xor %rdx, %rdx # div clear bits rdx 
idiv %rdi # div rax * rexp 
movq %rax, %r9 # move(t, t)
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rdi # mem(boe)
movq $8, %rdx # bin(t, i)
movq %r9, %rax # imul l -> rax
imul %rdx # imul rax * r 
add %rax, %rdi
movq (%rdi), %rax # mem(temp)
cmp %rcx, %rax
jl L9
L10:
movq -8(%rbp), %rdi # mem(boe)
movq %r9, %rdx # move reg arg 2 to temp
call L2
L11:
L14:
jmp L20
L12:
movq %rsi, %rax # move(t, t)
jmp L14
L9:
movq -8(%rbp), %rdi # mem(boe)
movq $1, %rax # bin(t, i)
add %rax, %r9
movq %r9, %rsi # move reg arg 1 to temp
movq %r8, %rdx # move reg arg 2 to temp
call L2
jmp L11
L20:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L15:
	.long  0x2
	.ascii "\n\0"
.text
L3:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L23:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rdi # mem(boe)
call L1
movq -8(%rbp), %rdi # mem(boe)
movq $0, %rsi # integerExpression
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdx # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %rdx
movq $7, %rcx # integerExpression
call L2
movq %rax, %rdi # rax to temp 
call printi
movq $L15, %rdi # default name
call print
jmp L22
L22:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# start 
L25:
movq %r15, -24(%rbp) # spill store
movq $16, -8(%rbp) # store to offset 2
movq $16, -8(%rbp) # store to offset 2
movq $-16, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
movq $0, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L3
movq -24(%rbp), %r15 # spill load
jmp L24
L24:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
