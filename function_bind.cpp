#include <iostream>
#include <functional>
#include <vector>

using MsgCallBack = std::function<void(int, std::vector<int>&)>;

class Device {
public:
    void SetCallBack(MsgCallBack cb)
    {
        std::cout << "IoMgr::SetCallBack()" << std::endl;
        cb_ = cb;
    }

    void SendMsg()
    {
        std::cout << "IoMgr::SendMsg()" << std::endl;
        std::vector<int> nums = {10, 20, 30};
        for (int i = 0; i < 10; i++) {
            cb_(i, nums);
        }
    }

private:
    MsgCallBack cb_;
};

class IoMgr {
public:
    void RecvMsg(int a, std::vector<int>& nums)
    {
        std::cout << "IoMgr RecvMsg: " << a << ":" << nums.size() << std::endl;
    }

    void Init()
    {
        std::cout << "IoMgr::Init()" << std::endl;
        dev_.SetCallBack(std::bind(&IoMgr::RecvMsg, this, std::placeholders::_1, std::placeholders::_2));
        dev_.SendMsg();
    }

private:
    Device dev_;
};


int main() {
    std::cout << "Hello, World!" << std::endl;
    IoMgr io;
    io.Init();

    return 0;
}
