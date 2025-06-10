import os

def save_multiple_folders_to_txt(folder_paths, output_file):
    with open(output_file, 'w', encoding='utf-8') as out:
        for folder_path in folder_paths:
            for root, dirs, files in os.walk(folder_path):
                for filename in files:
                    file_path = os.path.join(root, filename)
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            content = f.read()
                        relative_path = os.path.relpath(file_path, start=folder_path)
                        out.write(f'{os.path.join(folder_path, relative_path)}：\n【{content}】\n\n')
                    except Exception as e:
                        out.write(f'{file_path}：\n【无法读取文件：{e}】\n\n')

# 示例用法：指定多个文件夹
folders = ['./miniprogram', './typings']
output_file = 'all_contents.txt'

save_multiple_folders_to_txt(folders, output_file)
