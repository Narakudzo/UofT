//
//	ge.c
//	csc336 A2 Q3
//	Created by Spencer Ogawa on 2015-10-25
//	Copyright (c) 2015 Spencer. All rights reserved.
//

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int usage();

int main(int argc, char **argv)
{
    
    int partial = 0;
    int complete = 0;
    char *ptr;
    int c, n = 0;
    FILE *fp_a, *fp_b;
    
    while ((c = getopt (argc, argv, "cphn:")) != -1)
    {
        switch (c)
        {
            case 'p':
                partial = 1;
                break;
            case 'c':
                complete = 1;
                break;
            case 'n':
                n = strtol(optarg, &ptr, 10);
                break;
            default:
                usage(argv);
                return 0;
        }
    }
    if (n == 0)
    {
        usage(argv);
        return 0;
    }
    if (partial == 1 && complete == 1)
    {
        fprintf(stderr, "Please choose either -p for partial or -c for complete\n");
        return 1;
    }
    
    if((fp_a = fopen(argv[optind], "r")) == NULL)
    {
        fprintf(stderr, "%s: no such file\n", argv[optind]);
        exit(1);
    }
    if((fp_b = fopen(argv[optind+1], "r")) == NULL)
    {
        fprintf(stderr, "%s: no such file\n", argv[optind+1]);
        exit(1);
    }
    
    int i, j, x_index[n], index;
    double a[n][n], b[n], x_solution[n];
    for(i = 0; i < n; i++)
    {
        for(j = 0; j < n; j++)
        {
            fscanf(fp_a, "%lf", &a[i][j]);
        }
        fscanf(fp_b, "%lf", &b[i]);
        x_index[i] = i;
        x_solution[i] = 0;
    }
    
    
    // Print original matrix
    printf("\n[ORIGINAL MATRIX]\n");
    for(i = 0; i < n; i++)
    {
        printf("| ");
        for(j = 0; j < n; j++)
        {
            printf("%lf ", a[i][j]);
        }
        printf("| X%d\t| %lf\t|\n",x_index[i]+1, b[i]);
    }
    
    
    // Gauss Elimination
    int e, f, g;
    double factor;
    for(g = 0; g < n; g++)
    {
        // Partial and Complete pivoting interchange
        int k = 0, l = 0, m = 0, flag = 0;
        double temp_k = 0, temp_l = 0;
        if(partial == 1 || complete == 1)
        {
            
            for(i = g; i < n; i++)
            {
                k = i;
                l = i;
                temp_k = a[i][i];
                temp_l = a[i][i];
                for(j = i + 1; j < n; j++)
                {
                    
                    // Partial pivoting
                    if(temp_k < a[j][i])
                    {
                        k = j;
                        temp_k = a[j][i];
                    }
                    // Complete pivoting
                    if(temp_l < a[i][j])
                    {
                        l = j;
                        temp_l = a[i][j];
                    }
                }
                // Complete pivoting
                if(complete == 1 && l != i && temp_k < temp_l)
                {
                    for(m = 0; m < n; m++)
                    {
                        temp_l = a[m][l];
                        a[m][l] = a[m][i];
                        a[m][i] = temp_l;
                    }
                    index = x_index[i];
                    x_index[i] = x_index[l];
                    x_index[l] = index;
                    flag = 1;
                }
                // Partial pivoting (and complete pivoting)
                else if(k != i)
                {
                    for(m = 0; m < n; m++)
                    {
                        temp_k = a[k][m];
                        a[k][m] = a[i][m];
                        a[i][m] = temp_k;
                    }
                    temp_k = b[k];
                    b[k] = b[i];
                    b[i] = temp_k;
                    flag = 1;
                }
            }
            
            // Print matrix after partial or complete interchange
            if(flag == 1)
            {
                if(partial == 1)
                    printf("\n[AFTER PARTIAL INTERCHANGES %d]\n", g+1);
                else if(complete == 1)
                    printf("\n[AFTER COMPLETE INTERCHANGES %d]\n", g+1);
                for(i = 0; i < n; i++)
                {
                    printf("| ");
                    for(j = 0; j < n; j++)
                    {
                        printf("%lf ", a[i][j]);
                    }
                    printf("| X%d\t| %lf\t|\n",x_index[i]+1, b[i]);
                }
            }

        }
        
        for(e = g + 1; e < n; e++)
        {
            factor = a[e][g] / a[g][g];
            for(f =0; f < n; f++)
            {
                a[e][f] = a[e][f] - (a[g][f] * factor);
            }
            b[e] = b[e] - (b[g] * factor);
        }
        // Print matrix after Gauss Elimination
        printf("\n[AFTER GAUSS ELIMINATION %d]\n", g+1);
        for(i = 0; i < n; i++)
        {
            printf("| ");
            for(j = 0; j < n; j++)
            {
                printf("%lf ", a[i][j]);
            }
            printf("| X%d\t| %.6lf\t|\n",x_index[i]+1, b[i]);
        }
    }
    
    // Backward substitution
    printf("\n[SOLUTION]\n");
    for(i = n - 1; i >= 0; i--)
    {
        x_solution[i] = b[i] / a[i][i];
        // Update b[]
        for(j = i - 1; j  >= 0; j--)
        {
            b[j] = b[j] - (a[j][i] * x_solution[i]);
        }
        printf("X%d\t=\t%lf\n", x_index[i]+1, x_solution[i]);
    }
    
    return 0;
}


int usage(char **ptr)
{
    printf("Usage %s [-n num] [option] <matrix A> <matrix b>\n", ptr[0]);
    printf("Options:\n");
    printf("-c          Complete GE\n");
    printf("-p          Partial GE\n");
    printf("-n [num]    Specify number of matrix (n x n)\n");
    printf("If no option, elementary elimination will be used\n");
    printf("\nExample: %s -n 10 -p matA.txt matb.txt\n\n", ptr[0]);
    return 0;
}

