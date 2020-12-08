.global tigermain
.text
L2:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L4:
movq %rdi, -8(%rbp) # store to offset 1
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -16(%rax), %rax # mem(boe)
movq -8(%rbp), %rcx # mem(boe)
movq -8(%rcx), %rcx # mem(boe)
movq -16(%rcx), %rcx # mem(boe)
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L6:
movq %rdi, -8(%rbp) # store to offset 1
movq %rbp, %rdi # move reg arg 0 to temp
call L2
jmp L5
L5:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L8:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -16(%rbp) # store to offset 1
movq %rbp, %rdi # move reg arg 0 to temp
call L1
jmp L7
L7:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L10:
movq $10, %rsi # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq %rax, %rdi # rax to temp 
call printi
jmp L9
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
