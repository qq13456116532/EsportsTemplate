import os

def save_multiple_folders_to_txt(paths, output_file):
    with open(output_file, 'w', encoding='utf-8') as out:
        for path in paths:
            if os.path.isdir(path):
                for root, dirs, files in os.walk(path):
                    for filename in files:
                        file_path = os.path.join(root, filename)
                        try:
                            with open(file_path, 'r', encoding='utf-8') as f:
                                content = f.read()
                            relative_path = os.path.relpath(file_path, start=path)
                            out.write(f'{os.path.join(path, relative_path)}：\n【{content}】\n\n')
                        except Exception as e:
                            out.write(f'{file_path}：\n【无法读取文件：{e}】\n\n')
            elif os.path.isfile(path):
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        content = f.read()
                    out.write(f'{path}：\n【{content}】\n\n')
                except Exception as e:
                    out.write(f'{path}：\n【无法读取文件：{e}】\n\n')


# 示例用法：指定多个文件夹
folders = ['miniprogram/components', 'miniprogram/pages','miniprogram/utils']
output_file = 'all_contents.txt'

save_multiple_folders_to_txt(folders, output_file)
