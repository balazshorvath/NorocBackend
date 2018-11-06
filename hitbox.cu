extern "C"

__global__ void add(int n, float *sum) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i < n) {
        sum[i] = i;
    }
}
