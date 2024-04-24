.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# start 
L17:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, %rax # move(t, t)
movq %rax, -40(%rbp) # spill store
movq %rdx, -32(%rbp) # spill store
movq %r12, -24(%rbp) # spill store
movq %r13, -16(%rbp) # spill store
movq $0, %rcx # integerExpression
movq -40(%rbp), %rax # spill load
cmp %rcx, %rax
je L8
L9:
movq $0, %rax # integerExpression
movq -32(%rbp), %rdx # spill load
cmp %rax, %rdx
je L5
L6:
movq -40(%rbp), %rax # spill load
movq 0(%rax), %rax # mem(boe)
movq -32(%rbp), %rdx # spill load
movq 0(%rdx), %rcx # mem(boe)
cmp %rcx, %rax
jl L2
L3:
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r12 # rax to temp 
movq $0, %rcx # bin(t, i)
movq %r12, %rax # add lexp -> r
add %rcx, %rax
movq $0, %rcx # bin(t, i)
movq -32(%rbp), %rdx # spill load
add %rcx, %rdx
movq (%rdx), %rcx # m2m mem to temp
movq %rcx, (%rax) # m2m temp to mem
movq $8, %rax # bin(t, i)
movq %r12, %r13 # add lexp -> r
add %rax, %r13
movq -8(%rbp), %rdi # mem(boe)
movq -32(%rbp), %rdx # spill load
movq 8(%rdx), %rdx # mem(boe)
movq -40(%rbp), %rax # spill load
movq %rax, %rsi # move reg arg 1 to temp
call L0
movq %rax, (%r13) # store
movq %r12, %rax # move(t, t)
L4:
L7:
L10:
movq -24(%rbp), %r12 # spill load
movq -16(%rbp), %r13 # spill load
jmp L16
L8:
movq -32(%rbp), %rdx # spill load
movq %rdx, %rax # move(t, t)
jmp L10
L5:
movq -40(%rbp), %rax # spill load
jmp L7
L2:
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r12 # rax to temp 
movq $0, %rax # bin(t, i)
movq %r12, %rcx # add lexp -> r
add %rax, %rcx
movq $0, %rdx # bin(t, i)
movq -40(%rbp), %rax # spill load
add %rdx, %rax
movq (%rax), %rax # m2m mem to temp
movq %rax, (%rcx) # m2m temp to mem
movq $8, %rax # bin(t, i)
movq %r12, %r13 # add lexp -> r
add %rax, %r13
movq -8(%rbp), %rdi # mem(boe)
movq -40(%rbp), %rax # spill load
movq 8(%rax), %rsi # mem(boe)
movq -32(%rbp), %rdx # spill load
call L0
movq %rax, (%r13) # store
movq %r12, %rax # move(t, t)
jmp L4
L16:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L11:
	.long  0x2
	.ascii "\n\0"
.data
L12:
	.long  0x1
	.ascii " \0"
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L19:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -16(%rbp) # spill store
movq $0, %rax # integerExpression
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
je L13
L14:
movq -16(%rbp), %rsi # spill load
movq 0(%rsi), %rdi # mem(boe)
call printi
movq $L12, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq -16(%rbp), %rsi # spill load
movq 8(%rsi), %rsi # mem(boe)
call L1
L15:
jmp L18
L13:
movq $L11, %rdi # default name
call print
jmp L15
L18:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# start 
L21:
movq %rbx, -40(%rbp) # spill store
movq %r12, -32(%rbp) # spill store
movq %r13, -24(%rbp) # spill store
movq %r14, -16(%rbp) # spill store
movq %r15, -8(%rbp) # spill store
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r13 # rax to temp 
movq $2, 0(%r13) # store to offset 2
movq $8, %rax # bin(t, i)
movq %r13, %r12 # add lexp -> r
add %rax, %r12
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %rbx # rax to temp 
movq $4, 0(%rbx) # store to offset 2
movq $8, %rax # bin(t, i)
movq %rbx, %r14 # add lexp -> r
add %rax, %r14
movq $16, %rdi # integerExpression
call initRecord
movq $6, 0(%rax) # store to offset 2
movq $0, 8(%rax) # store to offset 2
movq %rax, (%r14) # store
movq %rbx, (%r12) # store
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r14 # rax to temp 
movq $3, 0(%r14) # store to offset 2
movq $8, %rax # bin(t, i)
movq %r14, %r15 # add lexp -> r
add %rax, %r15
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %rbx # rax to temp 
movq $5, 0(%rbx) # store to offset 2
movq $8, %rax # bin(t, i)
movq %rbx, %r12 # add lexp -> r
add %rax, %r12
movq $16, %rdi # integerExpression
call initRecord
movq $7, 0(%rax) # store to offset 2
movq $0, 8(%rax) # store to offset 2
movq %rax, (%r12) # store
movq %rbx, (%r15) # store
movq %rbp, %rdi # move reg arg 0 to temp
movq %r13, %rsi # move reg arg 1 to temp
movq %r14, %rdx # move reg arg 2 to temp
call L0
movq %rax, %rsi # rax to temp 
movq %rbp, %rdi # move reg arg 0 to temp
call L1
movq -40(%rbp), %rbx # spill load
movq -32(%rbp), %r12 # spill load
movq -24(%rbp), %r13 # spill load
movq -16(%rbp), %r14 # spill load
movq -8(%rbp), %r15 # spill load
jmp L20
L20:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
