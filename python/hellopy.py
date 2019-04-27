# name = input("pls input your name:")
# print(name)
print("hello world")
classmates = [["gjq", "gyz"], ["gjz", "fs", "fx"], "gjw"]
print(len(classmates))
print(classmates)
print(classmates[1][2])
tp = ("gjq", ["gjz", "fs", "fx"])
# error tp[0] = "gejianquan"
tp[1][2] = "fuxu"
print(tp[1][2])

sum1 = 0
for x in [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]:
    sum1 = sum1 + x
print(sum1)

sum2 = 0
x = 0
while x <= 100:
    sum2 = sum2 + x
    x = x + 1
print(sum2)

def pos1(x, y):
    return x, y

x, y = pos1(100, 200)
print(x, y)
