import os

from setuptools import find_packages, setup


with open(os.path.join(os.path.dirname(__file__), "README.rst")) as readme:
    long_description = readme.read()

classifiers = [
    "Development Status :: 3 - Alpha",
    "License :: OSI Approved :: MIT License",
    "Operating System :: OS Independent",
    "Programming Language :: Python",
    "Programming Language :: Python :: 2.7",
    "Programming Language :: Python :: 2",
    "Programming Language :: Python :: Implementation :: CPython",
    "Programming Language :: Python :: Implementation :: PyPy"
]

setup(
    name="sphinxcontrib-githubcomments",
    packages=find_packages(),
    namespace_packages=["sphinxcontrib"],
    setup_requires=["vcversioner"],
    vcversioner={
        "version_module_paths": ["sphinxcontrib/githubcomments/_version.py"],
    },
    include_package_data=True,
    author="Julian Berman",
    author_email="Julian@GrayVines.com",
    classifiers=classifiers,
    description="Add GitHub comments to your Sphinx docs.",
    license="MIT",
    long_description=long_description,
    url="https://github.com/Julian/sphinxcontrib-githubcomments",
)
