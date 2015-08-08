def hash(key):
    hash_list = []
    for i in range(0,key):
        hash_list.append((5*i) % 36)
    print(hash_list)
    hash_list.sort()
    print(hash_list)

