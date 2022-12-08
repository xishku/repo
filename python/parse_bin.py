import os
import shutil
import zipfile
from os.path import join, getsize
import time


class ParseBascZipTool:
    FILE_HEAD_LENGTH = 32
    DATA_HEAD = 12

    @staticmethod
    def get_files(find_dir, suffix):
        res = []
        for root, dirs, files in os.walk(find_dir):
            for filename in files:
                name, suf = os.path.splitext(filename)
                if suf == suffix:
                    res.append(os.path.join(root, filename))

        return res

    @staticmethod
    def unzip_file(zip_src, dst_dir):
        r = zipfile.is_zipfile(zip_src)
        if r:   
            fz = zipfile.ZipFile(zip_src, 'r')
            for file in fz.namelist():
                fz.extract(file, dst_dir)
        else:
            print('This is not zip')

    @staticmethod
    def decode_file(basc_src, stat):
        total_size = os.stat(basc_src).st_size
        # print("total_size", total_size)
        with open(basc_src, 'rb') as bin_file:
            index = 0
            head = bin_file.read(ParseBascZipTool.FILE_HEAD_LENGTH)
            ParseBascZipTool.head_handle(head)
            index += ParseBascZipTool.FILE_HEAD_LENGTH

            cnt = 0
            while index < total_size - 1: 
                data_head = bin_file.read(ParseBascZipTool.DATA_HEAD)
                data_size = ParseBascZipTool.data_head_handle(data_head, stat)
                data_block = bin_file.read(data_size - ParseBascZipTool.DATA_HEAD)
                ParseBascZipTool.can_frame_handle(data_block, stat)
                index += data_size
                cnt +=1
                # print("cnt=", cnt, index)

                # if cnt > 2:
                #     break

            print("cnt=", cnt)


    @staticmethod
    def head_handle(head):
        main_ver = head[0]
        minor_ver = head[1]
        # print("version: %d.%d" % (main_ver, minor_ver))
        file_type = head[3]
        # print("file_type: ", file_type)

        create_time = int.from_bytes(head[4:8], "little")
        us = int.from_bytes(head[8:12], "little")
        # print("create_time: ", create_time)
        time_array = time.localtime(create_time)  # 秒数
        f_time = time.strftime("%Y-%m-%d %H:%M:%S", time_array)
        # print(f_time, us)

    @staticmethod
    def data_head_handle(head, stat):
        # print(head)
        data_size = int.from_bytes(head[0:2], "little")
        # print("data_size:", data_size)
        combine_seg = int.from_bytes(head[2:3], "little")
        # print("combine_seg", combine_seg)
        data_type = (0b00000111 & combine_seg)
        # print("data_type:", data_type)
        data_dir = (0b00001000 & combine_seg) >> 3
        # print("data_dir:", data_dir)
        channel = (0b11110000 & combine_seg) >> 4
        # print("channel:", channel)

        create_time = int.from_bytes(head[4:8], "little")
        us = int.from_bytes(head[8:12], "little")
        # print("create_time: ", create_time)
        time_array = time.localtime(create_time)  # 秒数
        f_time = time.strftime("%Y-%m-%d %H:%M:%S", time_array)
        # print(f_time, us)

        if create_time not in stat.get(channel):
            stat.get(channel)[create_time] = 1
        else:
            stat.get(channel)[create_time] += 1

        return data_size


    @staticmethod
    def can_frame_handle(frame, stat):
        can_id = int.from_bytes(frame[0:4], "little")
        dlc = int.from_bytes(frame[4:5], "little")
        data = int.from_bytes(frame[8:16], "little")

        # print("0x%X %X 0x%16X" % (can_id, dlc, data))
        # /**
        # * struct can_frame - basic CAN frame structure
        # * @can_id:  CAN ID of the frame and CAN_*_FLAG flags, see canid_t definition
        # * @can_dlc: frame payload length in byte (0 .. 8) aka data length code
        # *           N.B. the DLC field from ISO 11898-1 Chapter 8.4.2.3 has a 1:1
        # *           mapping of the 'data length code' to the real payload length
        # * @__pad:   padding
        # * @__res0:  reserved / padding
        # * @__res1:  reserved / padding
        # * @data:    CAN frame payload (up to 8 byte)
        # */
        # struct can_frame {
        #     canid_t can_id;  /* 32 bit CAN_ID + EFF/RTR/ERR flags */
        #     __u8    can_dlc; /* frame payload length in byte (0 .. CAN_MAX_DLEN) */
        #     __u8    __pad;   /* padding */
        #     __u8    __res0;  /* reserved / padding */
        #     __u8    __res1;  /* reserved / padding */
        #     __u8    data[CAN_MAX_DLEN] __attribute__((aligned(8)));
        # };

    @staticmethod
    def gen_report(stat, report_file):
        with open(report_file, 'w') as txt_file:
            for port in stat.keys():
                for time_s in stat.get(port).keys():
                    line = ("can%d\t %s \t%d\n" % (port, time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(time_s)),
                        stat.get(port).get(time_s)))
                    txt_file.write(line)



if __name__ == "__main__":
    res = ParseBascZipTool.get_files("D:\\xxx\\data", ".zip")
    for zip_file in res:
        ParseBascZipTool.unzip_file(zip_file, "D:\\xxx\\data")

    basc_files = ParseBascZipTool.get_files("D:\\xxx\\data", ".basc")
    stat = dict()
    for port in range(8):
        stat[port] = dict()

    for basc in basc_files:
        print(basc)
        ParseBascZipTool.decode_file(basc, stat)
        # break

    ParseBascZipTool.gen_report(stat, "D:\\xxx\\report.txt")

    print("done")
