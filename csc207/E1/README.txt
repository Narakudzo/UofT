The file e1.pdf contains instructions for completing and submitting
exercise 1.

To open a pdf file on the lab machine, you can use the viewer evince:
  evince e1.pdf 

If you are logging in to CDF from home using ssh, then you
will have to use the -X option:

 ssh -X username@cdf.toronto.edu

Alternatively, you can transfer the file to your home computer by using 
the scp command:

 scp username@cdf.toronto.edu:/path/to/e1.pdf /some/local/path

... where /path/to/e1.pdf and /some/local/path are replaced with the
appropriate file paths.

Needless to say, we recommend that you use svn on your home computer
and avoid having to log in to the lab machine or to transfer files
every time!

Good luck with the exercise!
