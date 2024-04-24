.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $97, %rdi # integerExpression
call chr
movq %rax, %rdi # rax to temp 
call print
movq $98, %rdi # integerExpression
call chr
movq %rax, %rdi # rax to temp 
call print
movq $99, %rdi # integerExpression
call chr
movq %rax, %rdi # rax to temp 
call print
movq $100, %rdi # integerExpression
call chr
movq %rax, %rdi # move reg arg 0 to temp
call print
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
