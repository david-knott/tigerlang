.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
call flush
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
