FROM julia:1.1
RUN set -x \
    && apt-get update && apt-get install -y coinor-libsymphony-dev coinor-libcgl-dev libglpk-dev
COPY replenishment.jl /
COPY prep.jl /
RUN julia /prep.jl
EXPOSE 7000
ENTRYPOINT ["julia", "/replenishment.jl"]