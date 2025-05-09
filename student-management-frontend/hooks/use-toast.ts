// hooks/use-toast.ts
export function useToast() {
    return {
      toast: ({ title, description, variant }: { title: string; description: string; variant?: string }) => {
        console.log(`${variant || 'default'}: ${title} - ${description}`);
        // In a real app, you would use a toast library like react-hot-toast or sonner
        alert(`${title}: ${description}`);
      }
    };
  }