function createCombinations(input) {
    // if the input is empty, not an array or undefined, return an empty function
  if(!input || input.length < 1) {
    return [];
  }
  // if the array is the last subset, no more iterations has to be done
  // return the first element (which is the last array) as the iterations used
  // in the previous call
  if(Array.isArray(input[0]) && input.length == 1) {
    return input[0];
  }

  // Copy the Input to have a clean function
  const inputCopy =[...input];
  // The result will contain all available permutations for the given subset
  const result = [];
  // Collect the first Array of the subset of arrays to permutate through
  const firstArray = inputCopy.shift();
  // Recursive call, that creates all available permutations for the remaining arrays
  const subCombinations = createCombinations(inputCopy);
  // Combine each element of the first array with every permutation of the permutations
  // of the remaining subset
  for(let elem of firstArray)
  {
    for(let subComb of subCombinations) {
      result.push([elem].concat(subComb));
    }
  }

  // return the result
  return result;
}

const input =  [[1,2,3],['A','B','C'],['one','three']];
console.log(input)
console.log(createCombinations(input));
console.log(input)