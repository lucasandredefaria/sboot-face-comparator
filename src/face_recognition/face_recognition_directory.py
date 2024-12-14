import os
import face_recognition
import shutil
import sys
import json

def process_images_in_directory(directory_path):
    # Verifica se o diretório existe
    if not os.path.exists(directory_path):
        return json.dumps({"status": "error", "message": f"Diretório {directory_path} não encontrado."})

    # Lista todas as imagens no diretório
    image_files = [f for f in os.listdir(directory_path) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]

    # Lista para armazenar os encodings das faces
    face_encodings = []
    face_groups = []

    for image_file in image_files:
        image_path = os.path.join(directory_path, image_file)

        try:
            # Carregar a imagem
            image = face_recognition.load_image_file(image_path)
            encodings = face_recognition.face_encodings(image)

            # Se não houver face na imagem, pular para a próxima
            if not encodings:
                continue

            # Comparar a face com as já encontradas
            match_found = False
            for idx, known_encoding in enumerate(face_encodings):
                # Verifica se a face na imagem atual é igual a alguma face conhecida
                matches = face_recognition.compare_faces([known_encoding], encodings[0])
                if True in matches:
                    # Se encontrar uma face correspondente, adicionar a imagem ao grupo
                    face_groups[idx].append(image_file)
                    match_found = True
                    break

            # Se não encontrar nenhuma correspondência, adicionar uma nova face
            if not match_found:
                face_encodings.append(encodings[0])
                face_groups.append([image_file])

        except Exception as e:
            return json.dumps({"status": "error", "message": f"Erro ao processar a imagem {image_file}: {str(e)}"})

    # Organiza as imagens nas pastas
    output_dir = os.path.join(directory_path, "organized_faces")
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    for idx, group in enumerate(face_groups):
        # Cria uma pasta para cada grupo de faces
        group_folder = os.path.join(output_dir, f"group_{idx + 1}")
        os.makedirs(group_folder)

        for image_file in group:
            # Move as imagens para a pasta correspondente
            src_path = os.path.join(directory_path, image_file)
            dst_path = os.path.join(group_folder, image_file)
            shutil.move(src_path, dst_path)

    return json.dumps({"status": "success", "message": "Imagens organizadas com sucesso!"})

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Erro: Forneça o caminho do diretório como parâmetro.")
        sys.exit(1)

    directory_path = sys.argv[1]
    result = process_images_in_directory(directory_path)
    print(result)
