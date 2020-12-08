.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L11:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq $1, %rcx # integerExpression
cmp %rcx, %rax
je L7
L8:
movq -8(%rbp), %rax # mem(boe)
movq -16(%rax), %rax # mem(boe)
movq $2, %rcx # integerExpression
cmp %rcx, %rax
je L4
L5:
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -8(%rbp), %rcx # mem(boe)
movq -16(%rcx), %rcx # mem(boe)
cmp %rcx, %rax
jl L1
L2:
movq $4, %rax # move(t, i)
L3:
L6:
L9:
jmp L10
L7:
movq $1, %rax # move(t, i)
jmp L9
L4:
movq $2, %rax # move(t, i)
jmp L6
L1:
movq $3, %rax # move(t, i)
jmp L3
L10:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L13:
movq $5, -8(%rbp) # store to offset 2
movq $4, -16(%rbp) # store to offset 2
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq %rax, %rdi # rax to temp 
call printi
jmp L12
L12:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
